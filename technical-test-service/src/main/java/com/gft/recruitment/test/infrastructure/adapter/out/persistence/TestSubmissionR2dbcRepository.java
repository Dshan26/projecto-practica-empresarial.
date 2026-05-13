package com.gft.recruitment.test.infrastructure.adapter.out.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TestSubmissionR2dbcRepository extends ReactiveCrudRepository<TestSubmissionEntity, UUID> {
    Mono<TestSubmissionEntity> findByCandidateIdAndJobOfferId(UUID candidateId, UUID jobOfferId);
}
