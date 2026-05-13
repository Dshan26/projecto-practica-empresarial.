package com.gft.recruitment.evaluation.infrastructure.adapter.out.persistence;

import com.gft.recruitment.evaluation.domain.model.EvaluationResult;
import com.gft.recruitment.evaluation.domain.port.out.EvaluationRepositoryPort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class EvaluationRepositoryAdapter implements EvaluationRepositoryPort {

    private final EvaluationResultR2dbcRepository repository;

    public EvaluationRepositoryAdapter(EvaluationResultR2dbcRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<EvaluationResult> save(EvaluationResult result) {
        EvaluationResultEntity entity = toEntity(result);
        return repository.save(entity).map(this::toDomain);
    }

    @Override
    public Mono<EvaluationResult> findByCandidateAndJobOffer(UUID candidateId, UUID jobOfferId) {
        return repository.findByCandidateIdAndJobOfferId(candidateId, jobOfferId)
                .map(this::toDomain);
    }

    private EvaluationResultEntity toEntity(EvaluationResult result) {
        EvaluationResultEntity entity = new EvaluationResultEntity();
        entity.setId(result.getId());
        entity.setCandidateId(result.getCandidateId());
        entity.setJobOfferId(result.getJobOfferId());
        entity.setTestId(result.getTestId());
        entity.setTechScore(result.getTechScore());
        entity.setTotalQuestions(result.getTotalQuestions());
        entity.setCorrectAnswers(result.getCorrectAnswers());
        entity.setEvaluatedAt(result.getEvaluatedAt());
        entity.setNew(true);
        return entity;
    }

    private EvaluationResult toDomain(EvaluationResultEntity entity) {
        return new EvaluationResult(
                entity.getId(),
                entity.getCandidateId(),
                entity.getJobOfferId(),
                entity.getTestId(),
                entity.getTechScore(),
                entity.getTotalQuestions(),
                entity.getCorrectAnswers(),
                entity.getEvaluatedAt()
        );
    }
}
