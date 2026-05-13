package com.gft.recruitment.ai.application.service;

import com.gft.recruitment.ai.application.dto.OpenAiAnalysisResponse;
import com.gft.recruitment.ai.domain.model.*;
import com.gft.recruitment.ai.domain.port.in.AnalyzeCvUseCase;
import com.gft.recruitment.ai.domain.port.out.AnalysisResultStoragePort;
import com.gft.recruitment.ai.domain.port.out.OpenAiPort;
import com.gft.recruitment.events.CvUploadedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AiAnalysisService implements AnalyzeCvUseCase {

    private static final Logger log = LoggerFactory.getLogger(AiAnalysisService.class);

    private final OpenAiPort openAiPort;
    private final AnalysisResultStoragePort storagePort;
    private final WebClient serviceWebClient;

    public AiAnalysisService(OpenAiPort openAiPort,
                             AnalysisResultStoragePort storagePort,
                             @Qualifier("serviceWebClient") WebClient serviceWebClient) {
        this.openAiPort = openAiPort;
        this.storagePort = storagePort;
        this.serviceWebClient = serviceWebClient;
    }

    @Override
    public Mono<AnalysisResult> analyze(CvUploadedEvent event) {
        log.info("Starting CV analysis for cvId={}, candidateId={}, jobOfferId={}",
                event.cvId(), event.candidateId(), event.jobOfferId());

        AnalysisResult result = new AnalysisResult();
        result.setCvId(event.cvId());
        result.setCandidateId(event.candidateId());
        result.setJobOfferId(event.jobOfferId());
        result.setStatus(AnalysisStatus.PROCESSING);
        result.setRetryCount(0);

        return storagePort.save(result)
                .flatMap(saved -> fetchCvText(event.cvId())
                        .zipWith(fetchJobOfferRequirements(event.jobOfferId()))
                        .flatMap(tuple -> {
                            String cvText = tuple.getT1();
                            Map<String, Object> jobOffer = tuple.getT2();

                            @SuppressWarnings("unchecked")
                            List<String> requiredSkills = (List<String>) jobOffer.getOrDefault("habilidadesRequeridas", List.of());
                            String seniorityExpected = (String) jobOffer.getOrDefault("seniorityEsperado", "JUNIOR");

                            return openAiPort.analyzeCv(cvText, requiredSkills, seniorityExpected)
                                    .map(response -> mapToResult(saved, response));
                        })
                        .flatMap(storagePort::save)
                        .onErrorResume(error -> {
                            log.error("Analysis failed for cvId={}: {}", event.cvId(), error.getMessage());
                            saved.setStatus(AnalysisStatus.ERROR);
                            saved.setErrorMessage(error.getMessage());
                            saved.setAnalyzedAt(LocalDateTime.now());
                            return storagePort.save(saved);
                        })
                );
    }

    private Mono<String> fetchCvText(java.util.UUID cvId) {
        return serviceWebClient.get()
                .uri("http://cv-processing-service/api/cv/{id}/text", cvId)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> log.error("Failed to fetch CV text for cvId={}: {}", cvId, e.getMessage()));
    }

    @SuppressWarnings("unchecked")
    private Mono<Map<String, Object>> fetchJobOfferRequirements(java.util.UUID jobOfferId) {
        return serviceWebClient.get()
                .uri("http://job-offer-service/api/job-offers/{id}", jobOfferId)
                .retrieve()
                .bodyToMono(Map.class)
                .map(m -> (Map<String, Object>) m)
                .doOnError(e -> log.error("Failed to fetch job offer for jobOfferId={}: {}", jobOfferId, e.getMessage()));
    }

    private AnalysisResult mapToResult(AnalysisResult existing, OpenAiAnalysisResponse response) {
        List<IdentifiedSkill> skills = response.habilidadesTecnicas().stream()
                .map(dto -> new IdentifiedSkill(dto.nombre(), parseSkillLevel(dto.nivel())))
                .collect(Collectors.toList());

        existing.setHabilidadesIdentificadas(skills);
        existing.setAniosExperiencia(response.aniosExperiencia());
        existing.setSeniorityDetectado(parseSeniorityLevel(response.seniorityDetectado()));
        existing.setResumenProfesional(response.resumenProfesional());
        existing.setRawOpenAiResponse(response.rawResponse());
        existing.setStatus(AnalysisStatus.COMPLETED);
        existing.setAnalyzedAt(LocalDateTime.now());
        return existing;
    }

    private SkillLevel parseSkillLevel(String nivel) {
        try {
            return SkillLevel.valueOf(nivel.toUpperCase());
        } catch (IllegalArgumentException e) {
            return SkillLevel.BASICO;
        }
    }

    private SeniorityLevel parseSeniorityLevel(String seniority) {
        try {
            return SeniorityLevel.valueOf(seniority.toUpperCase());
        } catch (IllegalArgumentException e) {
            return SeniorityLevel.JUNIOR;
        }
    }
}
