package com.gft.recruitment.ranking.infrastructure.adapter.out.persistence;

import com.gft.recruitment.ranking.domain.model.RankingEntry;
import com.gft.recruitment.ranking.domain.port.out.RankingRepositoryPort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class RankingRepositoryAdapter implements RankingRepositoryPort {

    private final RankingEntryR2dbcRepository repository;

    public RankingRepositoryAdapter(RankingEntryR2dbcRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<RankingEntry> save(RankingEntry entry) {
        return repository.findByCandidateIdAndJobOfferId(entry.getCandidateId(), entry.getJobOfferId())
                .flatMap(existing -> {
                    existing.setCvScore(entry.getCvScore());
                    existing.setTechScore(entry.getTechScore());
                    existing.setFinalScore(entry.getFinalScore());
                    existing.setCalculatedAt(entry.getCalculatedAt());
                    return repository.save(existing);
                })
                .switchIfEmpty(Mono.defer(() -> repository.save(toEntity(entry))))
                .map(this::toDomain);
    }

    @Override
    public Flux<RankingEntry> findByJobOfferIdOrderByFinalScoreDesc(UUID jobOfferId) {
        return repository.findByJobOfferIdOrderByFinalScoreDescCvScoreDesc(jobOfferId)
                .map(this::toDomain);
    }

    @Override
    public Mono<RankingEntry> findByCandidateAndJobOffer(UUID candidateId, UUID jobOfferId) {
        return repository.findByCandidateIdAndJobOfferId(candidateId, jobOfferId)
                .map(this::toDomain);
    }

    @Override
    public Mono<Void> updateRankingPositions(UUID jobOfferId) {
        return repository.updateRankingPositionsByJobOfferId(jobOfferId);
    }

    private RankingEntryEntity toEntity(RankingEntry entry) {
        RankingEntryEntity entity = new RankingEntryEntity();
        entity.setId(entry.getId());
        entity.setCandidateId(entry.getCandidateId());
        entity.setJobOfferId(entry.getJobOfferId());
        entity.setCvScore(entry.getCvScore());
        entity.setTechScore(entry.getTechScore());
        entity.setFinalScore(entry.getFinalScore());
        entity.setRankingPosition(entry.getRankingPosition());
        entity.setCalculatedAt(entry.getCalculatedAt());
        entity.setNew(true);
        return entity;
    }

    private RankingEntry toDomain(RankingEntryEntity entity) {
        return new RankingEntry(
                entity.getId(),
                entity.getCandidateId(),
                entity.getJobOfferId(),
                entity.getCvScore(),
                entity.getTechScore(),
                entity.getFinalScore(),
                entity.getRankingPosition(),
                entity.getCalculatedAt()
        );
    }
}
