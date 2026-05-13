package com.gft.recruitment.joboffer.infrastructure.adapter.out.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface JobOfferSkillR2dbcRepository extends ReactiveCrudRepository<JobOfferSkillEntity, UUID> {
    Flux<JobOfferSkillEntity> findByJobOfferId(UUID jobOfferId);
    Mono<Void> deleteByJobOfferId(UUID jobOfferId);
}
