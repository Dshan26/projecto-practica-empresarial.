package com.gft.recruitment.test.domain.port.in;

import com.gft.recruitment.test.application.dto.TestQuestionsResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface GetTestQuestionsUseCase {
    Mono<TestQuestionsResponse> getQuestions(UUID jobOfferId, UUID candidateId);
}
