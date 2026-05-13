package com.gft.recruitment.ai.infrastructure.adapter.out.persistence;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AnalysisResultMongoRepository extends ReactiveMongoRepository<AnalysisResultDocument, String> {
    Mono<AnalysisResultDocument> findByCandidateIdAndJobOfferId(UUID candidateId, UUID jobOfferId);
}
