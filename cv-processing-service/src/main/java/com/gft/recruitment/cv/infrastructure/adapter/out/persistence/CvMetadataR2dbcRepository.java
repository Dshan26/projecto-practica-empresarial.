package com.gft.recruitment.cv.infrastructure.adapter.out.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CvMetadataR2dbcRepository extends ReactiveCrudRepository<CvMetadataEntity, UUID> {
    Mono<CvMetadataEntity> findByCandidateIdAndJobOfferId(UUID candidateId, UUID jobOfferId);
}
