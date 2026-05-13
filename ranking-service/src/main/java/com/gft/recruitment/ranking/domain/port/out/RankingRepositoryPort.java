package com.gft.recruitment.ranking.domain.port.out;

import com.gft.recruitment.ranking.domain.model.RankingEntry;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RankingRepositoryPort {
    Mono<RankingEntry> save(RankingEntry entry);
    Flux<RankingEntry> findByJobOfferIdOrderByFinalScoreDesc(UUID jobOfferId);
    Mono<RankingEntry> findByCandidateAndJobOffer(UUID candidateId, UUID jobOfferId);
    Mono<Void> updateRankingPositions(UUID jobOfferId);
}
