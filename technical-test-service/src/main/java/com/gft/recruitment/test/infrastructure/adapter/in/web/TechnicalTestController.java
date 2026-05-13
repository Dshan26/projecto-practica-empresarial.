package com.gft.recruitment.test.infrastructure.adapter.in.web;

import com.gft.recruitment.test.application.dto.SubmitAnswersCommand;
import com.gft.recruitment.test.application.dto.TestQuestionsResponse;
import com.gft.recruitment.test.domain.port.in.GetTestQuestionsUseCase;
import com.gft.recruitment.test.domain.port.in.SubmitAnswersUseCase;
import com.gft.recruitment.test.domain.port.out.TechnicalTestRepositoryPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/tests")
public class TechnicalTestController {

    private final GetTestQuestionsUseCase getTestQuestionsUseCase;
    private final SubmitAnswersUseCase submitAnswersUseCase;
    private final TechnicalTestRepositoryPort repositoryPort;

    public TechnicalTestController(GetTestQuestionsUseCase getTestQuestionsUseCase,
                                   SubmitAnswersUseCase submitAnswersUseCase,
                                   TechnicalTestRepositoryPort repositoryPort) {
        this.getTestQuestionsUseCase = getTestQuestionsUseCase;
        this.submitAnswersUseCase = submitAnswersUseCase;
        this.repositoryPort = repositoryPort;
    }

    @GetMapping("/{jobOfferId}")
    public Mono<TestQuestionsResponse> getQuestions(
            @PathVariable UUID jobOfferId,
            @RequestHeader(value = "X-User-Id", required = false) UUID candidateId) {
        return getTestQuestionsUseCase.getQuestions(jobOfferId, candidateId);
    }

    @PostMapping("/submit")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<Void> submitAnswers(
            @RequestBody SubmitAnswersCommand command,
            @RequestHeader(value = "X-User-Id", required = false) UUID candidateId) {
        SubmitAnswersCommand enriched = new SubmitAnswersCommand(
                command.testId(),
                candidateId != null ? candidateId : command.candidateId(),
                command.jobOfferId(),
                command.respuestas()
        );
        return submitAnswersUseCase.submit(enriched);
    }

    // Internal endpoints for evaluation-service
    @GetMapping("/internal/answers/{testId}/{candidateId}")
    public Flux<Map<String, Object>> getCandidateAnswers(
            @PathVariable UUID testId, @PathVariable UUID candidateId) {
        return repositoryPort.findAnswersByTestIdAndCandidateId(testId, candidateId)
                .map(a -> Map.<String, Object>of(
                        "questionId", a.getQuestionId(),
                        "respuesta", a.getRespuesta()
                ));
    }

    @GetMapping("/internal/questions/{testId}/answers")
    public Flux<Map<String, Object>> getCorrectAnswers(@PathVariable UUID testId) {
        return repositoryPort.findQuestionsByTestId(testId)
                .map(q -> Map.<String, Object>of(
                        "questionId", q.getId(),
                        "respuestaCorrecta", q.getRespuestaCorrecta()
                ));
    }

    @GetMapping("/check-submission/{jobOfferId}")
    public Mono<Map<String, Object>> checkSubmission(
            @PathVariable UUID jobOfferId,
            @RequestHeader(value = "X-User-Id", required = false) UUID candidateId) {
        if (candidateId == null) return Mono.just(Map.of("submitted", false));
        return repositoryPort.findSubmission(candidateId, jobOfferId)
                .map(s -> Map.<String, Object>of("submitted", true, "submittedAt", s.getSubmittedAt().toString()))
                .defaultIfEmpty(Map.of("submitted", false));
    }
}
