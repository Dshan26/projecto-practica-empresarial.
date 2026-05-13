package com.gft.recruitment.test.domain.port.out;

import com.gft.recruitment.test.domain.model.CandidateAnswer;
import com.gft.recruitment.test.domain.model.TechnicalTest;
import com.gft.recruitment.test.domain.model.TestQuestion;
import com.gft.recruitment.test.domain.model.TestSubmission;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TechnicalTestRepositoryPort {

    Mono<TechnicalTest> saveTest(TechnicalTest test);

    Mono<TestQuestion> saveQuestion(TestQuestion question);

    Mono<TechnicalTest> findTestByJobOfferId(UUID jobOfferId);

    Flux<TestQuestion> findQuestionsByTestId(UUID testId);

    Mono<CandidateAnswer> saveAnswer(CandidateAnswer answer);

    Mono<TestSubmission> saveSubmission(TestSubmission submission);

    Mono<TestSubmission> findSubmission(UUID candidateId, UUID jobOfferId);

    Flux<CandidateAnswer> findAnswersByTestIdAndCandidateId(UUID testId, UUID candidateId);
}
