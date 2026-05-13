package com.gft.recruitment.evaluation.domain.port.out;

import com.gft.recruitment.evaluation.domain.model.EvaluationResult;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface EvaluationRepositoryPort {
    Mono<EvaluationResult> save(EvaluationResult result);
    Mono<EvaluationResult> findByCandidateAndJobOffer(UUID candidateId, UUID jobOfferId);
}
