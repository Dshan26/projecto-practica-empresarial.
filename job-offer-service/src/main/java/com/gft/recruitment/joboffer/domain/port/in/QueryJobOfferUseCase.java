package com.gft.recruitment.joboffer.domain.port.in;

import com.gft.recruitment.joboffer.application.dto.JobOfferResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface QueryJobOfferUseCase {
    Mono<JobOfferResponse> findById(UUID id);
    Flux<JobOfferResponse> findAll();
}
