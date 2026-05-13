package com.gft.recruitment.joboffer.domain.port.out;

import com.gft.recruitment.joboffer.domain.model.JobOffer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface JobOfferRepositoryPort {
    Mono<JobOffer> save(JobOffer jobOffer);
    Mono<JobOffer> findById(UUID id);
    Flux<JobOffer> findAll();
    Mono<Void> deleteById(UUID id);
    Flux<String> findSkillsByJobOfferId(UUID jobOfferId);
    Mono<Void> saveSkills(UUID jobOfferId, List<String> skills);
    Mono<Void> deleteSkillsByJobOfferId(UUID jobOfferId);
}
