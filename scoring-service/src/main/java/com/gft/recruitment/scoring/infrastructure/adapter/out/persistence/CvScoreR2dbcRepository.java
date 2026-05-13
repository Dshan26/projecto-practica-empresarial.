package com.gft.recruitment.scoring.infrastructure.adapter.out.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CvScoreR2dbcRepository extends ReactiveCrudRepository<CvScoreEntity, UUID> {
    Mono<CvScoreEntity> findByCandidateIdAndJobOfferId(UUID candidateId, UUID jobOfferId);
}
