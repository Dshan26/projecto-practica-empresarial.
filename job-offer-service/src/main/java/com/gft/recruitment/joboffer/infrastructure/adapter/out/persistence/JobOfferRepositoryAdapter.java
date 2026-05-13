package com.gft.recruitment.joboffer.infrastructure.adapter.out.persistence;

import com.gft.recruitment.joboffer.domain.model.JobOffer;
import com.gft.recruitment.joboffer.domain.model.SeniorityLevel;
import com.gft.recruitment.joboffer.domain.port.out.JobOfferRepositoryPort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
public class JobOfferRepositoryAdapter implements JobOfferRepositoryPort {

    private final JobOfferR2dbcRepository jobOfferRepository;
    private final JobOfferSkillR2dbcRepository skillRepository;

    public JobOfferRepositoryAdapter(JobOfferR2dbcRepository jobOfferRepository,
                                     JobOfferSkillR2dbcRepository skillRepository) {
        this.jobOfferRepository = jobOfferRepository;
        this.skillRepository = skillRepository;
    }

    @Override
    public Mono<JobOffer> save(JobOffer jobOffer) {
        JobOfferEntity entity = toEntity(jobOffer);
        return jobOfferRepository.save(entity).map(this::toDomain);
    }

    @Override
    public Mono<JobOffer> findById(UUID id) {
        return jobOfferRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Flux<JobOffer> findAll() {
        return jobOfferRepository.findAll().map(this::toDomain);
    }

    @Override
    public Mono<Void> deleteById(UUID id) {
        return skillRepository.deleteByJobOfferId(id)
                .then(jobOfferRepository.deleteById(id));
    }

    @Override
    public Flux<String> findSkillsByJobOfferId(UUID jobOfferId) {
        return skillRepository.findByJobOfferId(jobOfferId)
                .map(JobOfferSkillEntity::getSkillName);
    }

    @Override
    public Mono<Void> saveSkills(UUID jobOfferId, List<String> skills) {
        return Flux.fromIterable(skills)
                .map(skill -> {
                    JobOfferSkillEntity entity = new JobOfferSkillEntity();
                    entity.setId(UUID.randomUUID());
                    entity.setJobOfferId(jobOfferId);
                    entity.setSkillName(skill);
                    entity.setNew(true);
                    return entity;
                })
                .flatMap(skillRepository::save)
                .then();
    }

    @Override
    public Mono<Void> deleteSkillsByJobOfferId(UUID jobOfferId) {
        return skillRepository.deleteByJobOfferId(jobOfferId);
    }

    private JobOfferEntity toEntity(JobOffer jobOffer) {
        JobOfferEntity entity = new JobOfferEntity();
        entity.setId(jobOffer.getId());
        entity.setNombreCargo(jobOffer.getNombreCargo());
        entity.setDescripcion(jobOffer.getDescripcion());
        entity.setExperienciaMinimaAnios(jobOffer.getExperienciaMinimaAnios());
        entity.setSeniorityEsperado(jobOffer.getSeniorityEsperado().name());
        entity.setRecruiterId(jobOffer.getRecruiterId());
        entity.setCreatedAt(jobOffer.getCreatedAt());
        entity.setUpdatedAt(jobOffer.getUpdatedAt());
        entity.setNew(true);
        return entity;
    }

    private JobOffer toDomain(JobOfferEntity entity) {
        return new JobOffer(
                entity.getId(),
                entity.getNombreCargo(),
                entity.getDescripcion(),
                null, // skills loaded separately
                entity.getExperienciaMinimaAnios(),
                SeniorityLevel.valueOf(entity.getSeniorityEsperado()),
                entity.getRecruiterId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
