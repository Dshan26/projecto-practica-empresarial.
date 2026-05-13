package com.gft.recruitment.evaluation.application.service;

import com.gft.recruitment.evaluation.application.dto.CandidateAnswerDTO;
import com.gft.recruitment.evaluation.application.dto.CvScoreDTO;
import com.gft.recruitment.evaluation.application.dto.QuestionAnswerDTO;
import com.gft.recruitment.evaluation.domain.model.EvaluationResult;
import com.gft.recruitment.evaluation.domain.port.in.EvaluateTestUseCase;
import com.gft.recruitment.evaluation.domain.port.out.EvaluationEventPublisherPort;
import com.gft.recruitment.evaluation.domain.port.out.EvaluationRepositoryPort;
import com.gft.recruitment.events.ScoresReadyEvent;
import com.gft.recruitment.events.TestCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EvaluationService implements EvaluateTestUseCase {

    private static final Logger log = LoggerFactory.getLogger(EvaluationService.class);

    private final EvaluationRepositoryPort evaluationRepository;
    private final EvaluationEventPublisherPort eventPublisher;
    private final WebClient serviceWebClient;

    public EvaluationService(EvaluationRepositoryPort evaluationRepository,
                             EvaluationEventPublisherPort eventPublisher,
                             @Qualifier("serviceWebClient") WebClient serviceWebClient) {
        this.evaluationRepository = evaluationRepository;
        this.eventPublisher = eventPublisher;
        this.serviceWebClient = serviceWebClient;
    }

    @Override
    public Mono<EvaluationResult> evaluate(TestCompletedEvent event) {
        log.info("Evaluating test for candidateId={}, jobOfferId={}, testId={}",
                event.candidateId(), event.jobOfferId(), event.testId());

        Mono<List<CandidateAnswerDTO>> candidateAnswersMono = fetchCandidateAnswers(
                event.testId(), event.candidateId());
        Mono<List<QuestionAnswerDTO>> correctAnswersMono = fetchCorrectAnswers(event.testId());

        return Mono.zip(candidateAnswersMono, correctAnswersMono)
                .flatMap(tuple -> {
                    List<CandidateAnswerDTO> candidateAnswers = tuple.getT1();
                    List<QuestionAnswerDTO> correctAnswers = tuple.getT2();

                    int totalQuestions = correctAnswers.size();
                    int correct = countCorrectAnswers(candidateAnswers, correctAnswers);
                    int techScore = totalQuestions > 0
                            ? (int) Math.round((correct * 100.0) / totalQuestions)
                            : 0;

                    log.info("Tech score calculated: {} ({}/{} correct) for candidateId={}",
                            techScore, correct, totalQuestions, event.candidateId());

                    EvaluationResult result = new EvaluationResult();
                    result.setId(UUID.randomUUID());
                    result.setCandidateId(event.candidateId());
                    result.setJobOfferId(event.jobOfferId());
                    result.setTestId(event.testId());
                    result.setTechScore(techScore);
                    result.setTotalQuestions(totalQuestions);
                    result.setCorrectAnswers(correct);
                    result.setEvaluatedAt(LocalDateTime.now());

                    return evaluationRepository.save(result)
                            .flatMap(saved -> fetchCvScore(event.candidateId(), event.jobOfferId())
                                    .flatMap(cvScore -> {
                                        ScoresReadyEvent scoresEvent = new ScoresReadyEvent(
                                                UUID.randomUUID(),
                                                Instant.now(),
                                                event.candidateId(),
                                                event.jobOfferId(),
                                                cvScore,
                                                techScore
                                        );
                                        return eventPublisher.publishScoresReady(scoresEvent)
                                                .thenReturn(saved);
                                    }));
                })
                .doOnError(e -> log.error("Evaluation failed for candidateId={}, testId={}: {}",
                        event.candidateId(), event.testId(), e.getMessage()));
    }

    private Mono<List<CandidateAnswerDTO>> fetchCandidateAnswers(UUID testId, UUID candidateId) {
        return serviceWebClient.get()
                .uri("http://technical-test-service/api/tests/internal/answers/{testId}/{candidateId}",
                        testId, candidateId)
                .retrieve()
                .bodyToFlux(CandidateAnswerDTO.class)
                .collectList()
                .doOnError(e -> log.error("Failed to fetch candidate answers: {}", e.getMessage()));
    }

    private Mono<List<QuestionAnswerDTO>> fetchCorrectAnswers(UUID testId) {
        return serviceWebClient.get()
                .uri("http://technical-test-service/api/tests/internal/questions/{testId}/answers", testId)
                .retrieve()
                .bodyToFlux(QuestionAnswerDTO.class)
                .collectList()
                .doOnError(e -> log.error("Failed to fetch correct answers: {}", e.getMessage()));
    }

    private Mono<Integer> fetchCvScore(UUID candidateId, UUID jobOfferId) {
        return serviceWebClient.get()
                .uri("http://scoring-service/api/scores/{candidateId}/{jobOfferId}",
                        candidateId, jobOfferId)
                .retrieve()
                .bodyToMono(CvScoreDTO.class)
                .map(CvScoreDTO::cvScore)
                .defaultIfEmpty(0)
                .doOnError(e -> log.error("Failed to fetch CV score: {}", e.getMessage()))
                .onErrorReturn(0);
    }

    int countCorrectAnswers(List<CandidateAnswerDTO> candidateAnswers,
                            List<QuestionAnswerDTO> correctAnswers) {
        Map<UUID, String> correctMap = correctAnswers.stream()
                .collect(Collectors.toMap(QuestionAnswerDTO::questionId,
                        QuestionAnswerDTO::respuestaCorrecta));

        int count = 0;
        for (CandidateAnswerDTO answer : candidateAnswers) {
            String correct = correctMap.get(answer.questionId());
            if (correct != null && correct.trim().equalsIgnoreCase(answer.respuesta().trim())) {
                count++;
            }
        }
        return count;
    }
}
