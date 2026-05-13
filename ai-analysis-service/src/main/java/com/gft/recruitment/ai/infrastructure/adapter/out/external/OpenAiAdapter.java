package com.gft.recruitment.ai.infrastructure.adapter.out.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gft.recruitment.ai.application.dto.OpenAiAnalysisResponse;
import com.gft.recruitment.ai.domain.exception.AnalysisFailedException;
import com.gft.recruitment.ai.domain.port.out.OpenAiPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class OpenAiAdapter implements OpenAiPort {

    private static final Logger log = LoggerFactory.getLogger(OpenAiAdapter.class);

    private final WebClient openAiWebClient;
    private final ObjectMapper objectMapper;

    @Value("${openai.api.model:gpt-4o-mini}")
    private String model;

    @Value("${openai.api.timeout:30}")
    private int timeoutSeconds;

    @Value("${openai.api.max-retries:3}")
    private int maxRetries;

    public OpenAiAdapter(@Qualifier("openAiWebClient") WebClient openAiWebClient,
                         ObjectMapper objectMapper) {
        this.openAiWebClient = openAiWebClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<OpenAiAnalysisResponse> analyzeCv(String cvText, List<String> requiredSkills, String seniorityExpected) {
        String prompt = buildPrompt(cvText, requiredSkills, seniorityExpected);

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", "Eres un experto en análisis de hojas de vida. Responde SOLO con JSON válido, sin texto adicional."),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.3
        );

        return openAiWebClient.post()
                .uri("/v1/chat/completions")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .retryWhen(Retry.backoff(maxRetries, Duration.ofSeconds(2))
                        .maxBackoff(Duration.ofSeconds(8))
                        .doBeforeRetry(signal -> log.warn("Retrying OpenAI call, attempt {}", signal.totalRetries() + 1)))
                .flatMap(this::parseResponse)
                .doOnSuccess(r -> log.info("OpenAI analysis completed successfully"))
                .doOnError(e -> log.error("OpenAI analysis failed: {}", e.getMessage()));
    }

    private String buildPrompt(String cvText, List<String> requiredSkills, String seniorityExpected) {
        return """
                Analiza la siguiente hoja de vida y extrae la información solicitada.
                
                HOJA DE VIDA:
                %s
                
                HABILIDADES REQUERIDAS PARA EL CARGO: %s
                SENIORITY ESPERADO: %s
                
                Responde ÚNICAMENTE con un JSON con la siguiente estructura:
                {
                  "habilidadesTecnicas": [
                    {"nombre": "nombre_habilidad", "nivel": "BASICO|INTERMEDIO|AVANZADO"}
                  ],
                  "aniosExperiencia": numero_entero,
                  "seniorityDetectado": "JUNIOR|SEMI_SENIOR|SENIOR",
                  "resumenProfesional": "resumen breve del perfil profesional"
                }
                """.formatted(cvText, String.join(", ", requiredSkills), seniorityExpected);
    }

    private Mono<OpenAiAnalysisResponse> parseResponse(String rawResponse) {
        try {
            JsonNode root = objectMapper.readTree(rawResponse);
            JsonNode choices = root.path("choices");
            if (choices.isEmpty() || choices.isMissingNode()) {
                return Mono.error(new AnalysisFailedException("No choices in OpenAI response"));
            }

            String content = choices.get(0).path("message").path("content").asText();

            // Clean potential markdown code block wrapping
            String jsonContent = content.strip();
            if (jsonContent.startsWith("```json")) {
                jsonContent = jsonContent.substring(7);
            } else if (jsonContent.startsWith("```")) {
                jsonContent = jsonContent.substring(3);
            }
            if (jsonContent.endsWith("```")) {
                jsonContent = jsonContent.substring(0, jsonContent.length() - 3);
            }
            jsonContent = jsonContent.strip();

            JsonNode analysisNode = objectMapper.readTree(jsonContent);

            List<OpenAiAnalysisResponse.SkillDto> skills = new ArrayList<>();
            JsonNode skillsNode = analysisNode.path("habilidadesTecnicas");
            if (skillsNode.isArray()) {
                for (JsonNode skillNode : skillsNode) {
                    skills.add(new OpenAiAnalysisResponse.SkillDto(
                            skillNode.path("nombre").asText(),
                            skillNode.path("nivel").asText("BASICO")
                    ));
                }
            }

            return Mono.just(new OpenAiAnalysisResponse(
                    skills,
                    analysisNode.path("aniosExperiencia").asInt(0),
                    analysisNode.path("seniorityDetectado").asText("JUNIOR"),
                    analysisNode.path("resumenProfesional").asText(""),
                    rawResponse
            ));
        } catch (JsonProcessingException e) {
            return Mono.error(new AnalysisFailedException("Failed to parse OpenAI response: " + e.getMessage(), e));
        }
    }
}
