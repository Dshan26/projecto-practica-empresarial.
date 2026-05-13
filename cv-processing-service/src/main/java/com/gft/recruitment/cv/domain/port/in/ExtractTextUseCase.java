package com.gft.recruitment.cv.domain.port.in;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ExtractTextUseCase {
    Mono<String> extractText(UUID cvId);
}
