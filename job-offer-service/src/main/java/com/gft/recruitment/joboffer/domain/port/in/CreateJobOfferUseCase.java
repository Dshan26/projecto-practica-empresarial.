package com.gft.recruitment.joboffer.domain.port.in;

import com.gft.recruitment.joboffer.application.dto.CreateJobOfferCommand;
import com.gft.recruitment.joboffer.application.dto.JobOfferResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CreateJobOfferUseCase {
    Mono<JobOfferResponse> create(CreateJobOfferCommand command, UUID recruiterId);
}
