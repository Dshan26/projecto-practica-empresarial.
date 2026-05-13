package com.gft.recruitment.test.infrastructure.adapter.out.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TechnicalTestR2dbcRepository extends ReactiveCrudRepository<TechnicalTestEntity, UUID> {
    Mono<TechnicalTestEntity> findByJobOfferId(UUID jobOfferId);
}
