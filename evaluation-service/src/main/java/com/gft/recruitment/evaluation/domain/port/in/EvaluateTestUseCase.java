package com.gft.recruitment.evaluation.domain.port.in;

import com.gft.recruitment.evaluation.domain.model.EvaluationResult;
import com.gft.recruitment.events.TestCompletedEvent;
import reactor.core.publisher.Mono;

public interface EvaluateTestUseCase {
    Mono<EvaluationResult> evaluate(TestCompletedEvent event);
}
