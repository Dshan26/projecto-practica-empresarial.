package com.gft.recruitment.test.domain.port.in;

import com.gft.recruitment.test.application.dto.SubmitAnswersCommand;
import reactor.core.publisher.Mono;

public interface SubmitAnswersUseCase {
    Mono<Void> submit(SubmitAnswersCommand command);
}
