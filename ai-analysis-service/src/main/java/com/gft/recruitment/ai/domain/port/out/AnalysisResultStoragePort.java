package com.gft.recruitment.ai.domain.port.out;

import com.gft.recruitment.ai.domain.model.AnalysisResult;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AnalysisResultStoragePort {
    Mono<AnalysisResult> save(AnalysisResult result);
    Mono<AnalysisResult> findByCandidateAndJobOffer(UUID candidateId, UUID jobOfferId);
}
