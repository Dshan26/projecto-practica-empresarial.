package com.gft.recruitment.evaluation.infrastructure.adapter.out.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface EvaluationResultR2dbcRepository extends ReactiveCrudRepository<EvaluationResultEntity, UUID> {
    Mono<EvaluationResultEntity> findByCandidateIdAndJobOfferId(UUID candidateId, UUID jobOfferId);
}
