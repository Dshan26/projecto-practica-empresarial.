package com.gft.recruitment.test.application.service;

import com.gft.recruitment.events.TestCompletedEvent;
import com.gft.recruitment.test.application.dto.*;
import com.gft.recruitment.test.domain.exception.IncompleteAnswersException;
import com.gft.recruitment.test.domain.exception.TestAlreadySubmittedException;
import com.gft.recruitment.test.domain.exception.TestNotFoundException;
import com.gft.recruitment.test.domain.model.CandidateAnswer;
import com.gft.recruitment.test.domain.model.TechnicalTest;
import com.gft.recruitment.test.domain.model.TestSubmission;
import com.gft.recruitment.test.domain.port.in.GenerateTestUseCase;
import com.gft.recruitment.test.domain.port.in.GetTestQuestionsUseCase;
import com.gft.recruitment.test.domain.port.in.SubmitAnswersUseCase;
import com.gft.recruitment.test.domain.port.out.TechnicalTestRepositoryPort;
import com.gft.recruitment.test.domain.port.out.TestEventPublisherPort;
import com.gft.recruitment.test.infrastructure.adapter.out.external.OpenAiTestGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TechnicalTestService implements GenerateTestUseCase, GetTestQuestionsUseCase, SubmitAnswersUseCase {

    private static final Logger log = LoggerFactory.getLogger(TechnicalTestService.class);

    private final TechnicalTestRepositoryPort repositoryPort;
    private final TestEventPublisherPort eventPublisherPort;
    private final OpenAiTestGenerator openAiTestGenerator;

    public TechnicalTestService(TechnicalTestRepositoryPort repositoryPort,
                                TestEventPublisherPort eventPublisherPort,
                                OpenAiTestGenerator openAiTestGenerator) {
        this.repositoryPort = repositoryPort;
        this.eventPublisherPort = eventPublisherPort;
        this.openAiTestGenerator = openAiTestGenerator;
    }

    @Override
    public Mono<TechnicalTest> generate(UUID jobOfferId) {
        log.info("Generating technical test for jobOfferId={}", jobOfferId);
        return repositoryPort.findTestByJobOfferId(jobOfferId)
                .switchIfEmpty(
                        openAiTestGenerator.generateTest(jobOfferId)
                                .flatMap(testWithQuestions -> {
                                    TechnicalTest test = testWithQuestions.test();
                                    return repositoryPort.saveTest(test)
                                            .flatMap(savedTest ->
                                                    Flux.fromIterable(testWithQuestions.questions())
                                                            .flatMap(q -> {
                                                                q.setTestId(savedTest.getId());
                                                                return repositoryPort.saveQuestion(q);
                                                            })
                                                            .then(Mono.just(savedTest))
                                            );
                                })
                );
    }

    @Override
    public Mono<TestQuestionsResponse> getQuestions(UUID jobOfferId, UUID candidateId) {
        log.info("Getting test questions for jobOfferId={}, candidateId={}", jobOfferId, candidateId);
        return repositoryPort.findTestByJobOfferId(jobOfferId)
                .switchIfEmpty(
                        generate(jobOfferId)
                )
                .flatMap(test ->
                        repositoryPort.findQuestionsByTestId(test.getId())
                                .map(q -> new QuestionDTO(
                                        q.getId(),
                                        q.getEnunciado(),
                                        q.getTipo(),
                                        q.getOpciones()
                                ))
                                .collectList()
                                .map(questions -> new TestQuestionsResponse(
                                        test.getId(),
                                        test.getJobOfferId(),
                                        questions
                                ))
                );
    }

    @Override
    public Mono<Void> submit(SubmitAnswersCommand command) {
        log.info("Submitting answers for testId={}, candidateId={}, jobOfferId={}",
                command.testId(), command.candidateId(), command.jobOfferId());

        return repositoryPort.findSubmission(command.candidateId(), command.jobOfferId())
                .flatMap(existing -> Mono.<Void>error(new TestAlreadySubmittedException(
                        "Candidate " + command.candidateId() + " already submitted test for job offer " + command.jobOfferId())))
                .switchIfEmpty(
                        repositoryPort.findQuestionsByTestId(command.testId())
                                .collectList()
                                .flatMap(questions -> {
                                    Set<UUID> questionIds = questions.stream()
                                            .map(q -> q.getId())
                                            .collect(Collectors.toSet());
                                    Set<UUID> answeredIds = command.respuestas().stream()
                                            .map(AnswerDTO::questionId)
                                            .collect(Collectors.toSet());

                                    if (!answeredIds.containsAll(questionIds)) {
                                        return Mono.error(new IncompleteAnswersException(
                                                "All questions must be answered. Expected " + questionIds.size()
                                                        + " answers, received " + answeredIds.size()));
                                    }

                                    LocalDateTime now = LocalDateTime.now();

                                    return Flux.fromIterable(command.respuestas())
                                            .flatMap(answer -> {
                                                CandidateAnswer ca = new CandidateAnswer(
                                                        UUID.randomUUID(),
                                                        command.testId(),
                                                        command.candidateId(),
                                                        command.jobOfferId(),
                                                        answer.questionId(),
                                                        answer.respuesta(),
                                                        now
                                                );
                                                return repositoryPort.saveAnswer(ca);
                                            })
                                            .then(Mono.defer(() -> {
                                                TestSubmission submission = new TestSubmission(
                                                        UUID.randomUUID(),
                                                        command.testId(),
                                                        command.candidateId(),
                                                        command.jobOfferId(),
                                                        now
                                                );
                                                return repositoryPort.saveSubmission(submission);
                                            }))
                                            .then(Mono.defer(() -> {
                                                TestCompletedEvent event = new TestCompletedEvent(
                                                        UUID.randomUUID(),
                                                        Instant.now(),
                                                        command.testId(),
                                                        command.candidateId(),
                                                        command.jobOfferId()
                                                );
                                                return eventPublisherPort.publishTestCompleted(event);
                                            }));
                                })
                );
    }
}
