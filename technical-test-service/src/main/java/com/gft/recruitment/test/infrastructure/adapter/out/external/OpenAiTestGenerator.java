package com.gft.recruitment.test.infrastructure.adapter.out.external;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gft.recruitment.test.domain.model.QuestionType;
import com.gft.recruitment.test.domain.model.TechnicalTest;
import com.gft.recruitment.test.domain.model.TestQuestion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class OpenAiTestGenerator {

    private static final Logger log = LoggerFactory.getLogger(OpenAiTestGenerator.class);

    private final WebClient openAiWebClient;
    private final WebClient serviceWebClient;
    private final ObjectMapper objectMapper;

    @Value("${openai.model:gpt-4o-mini}")
    private String model;

    public OpenAiTestGenerator(@Qualifier("openAiWebClient") WebClient openAiWebClient,
                               @Qualifier("serviceWebClient") WebClient serviceWebClient,
                               ObjectMapper objectMapper) {
        this.openAiWebClient = openAiWebClient;
        this.serviceWebClient = serviceWebClient;
        this.objectMapper = objectMapper;
    }

    public Mono<TestWithQuestions> generateTest(UUID jobOfferId) {
        return fetchJobOfferSkills(jobOfferId)
                .flatMap(this::callOpenAi)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)).maxBackoff(Duration.ofSeconds(8)))
                .map(response -> parseResponse(jobOfferId, response));
    }

    private Mono<List<String>> fetchJobOfferSkills(UUID jobOfferId) {
        return serviceWebClient.get()
                .uri("http://job-offer-service/api/job-offers/{id}", jobOfferId)
                .retrieve()
                .bodyToMono(Map.class)
                .map(body -> {
                    Object skills = body.get("habilidadesRequeridas");
                    if (skills instanceof List<?> list) {
                        return list.stream().map(Object::toString).toList();
                    }
                    return List.of("General Programming");
                })
                .onErrorResume(e -> {
                    log.warn("Could not fetch job offer skills for {}: {}", jobOfferId, e.getMessage());
                    return Mono.just(List.of("General Programming"));
                });
    }

    private Mono<String> callOpenAi(List<String> skills) {
        String skillsList = String.join(", ", skills);
        String prompt = """
                Genera una prueba técnica con EXACTAMENTE 10 preguntas para un candidato a desarrollador.
                Habilidades requeridas: %s.
                
                La prueba DEBE incluir:
                - 3 preguntas de opción múltiple (MULTIPLE_CHOICE) sobre conceptos teóricos
                - 4 preguntas abiertas de PROGRAMACIÓN (OPEN_ENDED) donde el candidato debe escribir código real
                - 3 preguntas abiertas de análisis y diseño (OPEN_ENDED)
                
                Para las preguntas de programación, pide al candidato que:
                - Implemente funciones o métodos específicos
                - Resuelva problemas algorítmicos escribiendo código
                - Corrija bugs en fragmentos de código
                - Diseñe clases o interfaces
                
                IMPORTANTE: Las preguntas de código deben ser lo suficientemente específicas para detectar si el candidato realmente sabe programar o si usó IA para generar las respuestas. Incluye restricciones específicas como "sin usar streams" o "usando solo recursión" para dificultar respuestas genéricas de IA.
                
                Retorna un JSON array con estos campos:
                - "enunciado": texto de la pregunta en español (para preguntas de código, incluye el contexto y restricciones)
                - "tipo": "MULTIPLE_CHOICE" o "OPEN_ENDED"
                - "opciones": array de 4 opciones (null para OPEN_ENDED)
                - "respuestaCorrecta": la respuesta correcta (para código, incluye la solución esperada)
                - "orden": número de pregunta (1-10)
                
                Retorna SOLO el JSON array, sin texto adicional.
                """.formatted(skillsList);

        Map<String, Object> request = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a technical test generator. Return only valid JSON."),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.7
        );

        return openAiWebClient.post()
                .uri("/v1/chat/completions")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                });
    }

    private TestWithQuestions parseResponse(UUID jobOfferId, String jsonContent) {
        UUID testId = UUID.randomUUID();
        TechnicalTest test = new TechnicalTest(testId, jobOfferId, LocalDateTime.now());

        List<TestQuestion> questions = new ArrayList<>();
        try {
            String cleaned = jsonContent.trim();
            if (cleaned.startsWith("```")) {
                cleaned = cleaned.replaceAll("```json\\s*", "").replaceAll("```\\s*", "");
            }
            List<Map<String, Object>> parsed = objectMapper.readValue(cleaned, new TypeReference<>() {});
            for (Map<String, Object> q : parsed) {
                List<String> opciones = null;
                if (q.get("opciones") != null) {
                    opciones = ((List<?>) q.get("opciones")).stream().map(Object::toString).toList();
                }
                QuestionType tipo = QuestionType.valueOf((String) q.get("tipo"));
                questions.add(new TestQuestion(
                        UUID.randomUUID(),
                        testId,
                        (String) q.get("enunciado"),
                        tipo,
                        opciones,
                        (String) q.get("respuestaCorrecta"),
                        q.get("orden") instanceof Number n ? n.intValue() : questions.size() + 1
                ));
            }
        } catch (Exception e) {
            log.error("Failed to parse OpenAI response, generating fallback questions", e);
            questions = generateFallbackQuestions(testId);
        }

        return new TestWithQuestions(test, questions);
    }

    private List<TestQuestion> generateFallbackQuestions(UUID testId) {
        List<TestQuestion> questions = new ArrayList<>();
        questions.add(new TestQuestion(UUID.randomUUID(), testId,
                "¿Cuál es la diferencia entre una clase abstracta y una interfaz?",
                QuestionType.OPEN_ENDED, null, "Una clase abstracta puede tener implementación parcial, una interfaz solo define contratos.", 1));
        questions.add(new TestQuestion(UUID.randomUUID(), testId,
                "¿Qué patrón de diseño se usa para crear objetos sin especificar la clase exacta?",
                QuestionType.MULTIPLE_CHOICE, List.of("Singleton", "Factory", "Observer", "Strategy"),
                "Factory", 2));
        questions.add(new TestQuestion(UUID.randomUUID(), testId,
                "¿Qué significa SOLID en programación orientada a objetos?",
                QuestionType.OPEN_ENDED, null, "Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, Dependency Inversion.", 3));
        questions.add(new TestQuestion(UUID.randomUUID(), testId,
                "¿Cuál de las siguientes NO es una estructura de datos lineal?",
                QuestionType.MULTIPLE_CHOICE, List.of("Array", "Lista enlazada", "Árbol binario", "Cola"),
                "Árbol binario", 4));
        questions.add(new TestQuestion(UUID.randomUUID(), testId,
                "¿Cuál es la complejidad temporal de la búsqueda binaria?",
                QuestionType.MULTIPLE_CHOICE, List.of("O(n)", "O(log n)", "O(n²)", "O(1)"),
                "O(log n)", 5));
        return questions;
    }

    public record TestWithQuestions(TechnicalTest test, List<TestQuestion> questions) {
    }
}
