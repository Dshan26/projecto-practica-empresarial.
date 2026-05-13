package com.gft.recruitment.scoring.domain.port.out;

import com.gft.recruitment.scoring.domain.model.CvScore;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CvScoreRepositoryPort {
    Mono<CvScore> save(CvScore cvScore);
    Mono<CvScore> findByCandidateAndJobOffer(UUID candidateId, UUID jobOfferId);
}
