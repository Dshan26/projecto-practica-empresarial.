package com.gft.recruitment.joboffer.infrastructure.adapter.out.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface JobOfferR2dbcRepository extends ReactiveCrudRepository<JobOfferEntity, UUID> {
}
