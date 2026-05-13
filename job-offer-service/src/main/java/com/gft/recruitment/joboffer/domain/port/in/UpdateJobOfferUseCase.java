package com.gft.recruitment.joboffer.domain.port.in;

import com.gft.recruitment.joboffer.application.dto.JobOfferResponse;
import com.gft.recruitment.joboffer.application.dto.UpdateJobOfferCommand;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UpdateJobOfferUseCase {
    Mono<JobOfferResponse> update(UUID id, UpdateJobOfferCommand command);
}
