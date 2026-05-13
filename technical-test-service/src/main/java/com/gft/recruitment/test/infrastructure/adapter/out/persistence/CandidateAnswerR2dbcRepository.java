package com.gft.recruitment.test.infrastructure.adapter.out.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface CandidateAnswerR2dbcRepository extends ReactiveCrudRepository<CandidateAnswerEntity, UUID> {
    Flux<CandidateAnswerEntity> findByTestIdAndCandidateId(UUID testId, UUID candidateId);
}
