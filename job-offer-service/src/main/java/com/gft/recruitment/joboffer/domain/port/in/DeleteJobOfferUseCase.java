package com.gft.recruitment.joboffer.domain.port.in;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface DeleteJobOfferUseCase {
    Mono<Void> delete(UUID id);
}
