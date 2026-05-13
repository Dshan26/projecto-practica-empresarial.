package com.gft.recruitment.ranking.infrastructure.adapter.out.persistence;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RankingEntryR2dbcRepository extends ReactiveCrudRepository<RankingEntryEntity, UUID> {

    Flux<RankingEntryEntity> findByJobOfferIdOrderByFinalScoreDescCvScoreDesc(UUID jobOfferId);

    Mono<RankingEntryEntity> findByCandidateIdAndJobOfferId(UUID candidateId, UUID jobOfferId);

    @Query("UPDATE ranking_entries SET ranking_position = sub.pos " +
           "FROM (SELECT id, ROW_NUMBER() OVER (ORDER BY final_score DESC, cv_score DESC) AS pos " +
           "      FROM ranking_entries WHERE job_offer_id = :jobOfferId) AS sub " +
           "WHERE ranking_entries.id = sub.id AND ranking_entries.job_offer_id = :jobOfferId")
    Mono<Void> updateRankingPositionsByJobOfferId(UUID jobOfferId);
}
