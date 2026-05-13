package com.gft.recruitment.test.domain.port.in;

import com.gft.recruitment.test.domain.model.TechnicalTest;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface GenerateTestUseCase {
    Mono<TechnicalTest> generate(UUID jobOfferId);
}
