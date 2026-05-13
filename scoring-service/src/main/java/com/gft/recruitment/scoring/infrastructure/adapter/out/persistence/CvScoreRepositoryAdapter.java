package com.gft.recruitment.scoring.infrastructure.adapter.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gft.recruitment.scoring.domain.model.CvScore;
import com.gft.recruitment.scoring.domain.model.ScoreDetails;
import com.gft.recruitment.scoring.domain.port.out.CvScoreRepositoryPort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CvScoreRepositoryAdapter implements CvScoreRepositoryPort {

    private final CvScoreR2dbcRepository repository;
    private final ObjectMapper objectMapper;

    public CvScoreRepositoryAdapter(CvScoreR2dbcRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<CvScore> save(CvScore cvScore) {
        CvScoreEntity entity = toEntity(cvScore);
        return repository.save(entity).map(this::toDomain);
    }

    @Override
    public Mono<CvScore> findByCandidateAndJobOffer(UUID candidateId, UUID jobOfferId) {
        return repository.findByCandidateIdAndJobOfferId(candidateId, jobOfferId)
                .map(this::toDomain);
    }

    private CvScoreEntity toEntity(CvScore cvScore) {
        CvScoreEntity entity = new CvScoreEntity();
        entity.setId(cvScore.getId());
        entity.setCandidateId(cvScore.getCandidateId());
        entity.setJobOfferId(cvScore.getJobOfferId());
        entity.setCvScore(cvScore.getCvScore());
        entity.setAnalysisError(cvScore.isAnalysisError());
        entity.setCalculatedAt(cvScore.getCalculatedAt());
        try {
            entity.setScoreDetails(objectMapper.writeValueAsString(cvScore.getDetails()));
        } catch (JsonProcessingException e) {
            entity.setScoreDetails("{}");
        }
        entity.setNew(true);
        return entity;
    }

    private CvScore toDomain(CvScoreEntity entity) {
        ScoreDetails details = null;
        try {
            if (entity.getScoreDetails() != null) {
                details = objectMapper.readValue(entity.getScoreDetails(), ScoreDetails.class);
            }
        } catch (JsonProcessingException e) {
            details = new ScoreDetails(0, 0, 0);
        }
        return new CvScore(
                entity.getId(),
                entity.getCandidateId(),
                entity.getJobOfferId(),
                entity.getCvScore(),
                details,
                entity.isAnalysisError(),
                entity.getCalculatedAt()
        );
    }
}
