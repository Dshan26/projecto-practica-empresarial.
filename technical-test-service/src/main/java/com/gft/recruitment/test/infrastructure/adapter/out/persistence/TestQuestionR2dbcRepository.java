package com.gft.recruitment.test.infrastructure.adapter.out.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface TestQuestionR2dbcRepository extends ReactiveCrudRepository<TestQuestionEntity, UUID> {
    Flux<TestQuestionEntity> findByTestIdOrderByOrdenAsc(UUID testId);
}
