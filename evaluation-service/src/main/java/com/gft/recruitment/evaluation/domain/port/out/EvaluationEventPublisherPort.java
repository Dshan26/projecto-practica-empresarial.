package com.gft.recruitment.evaluation.domain.port.out;

import com.gft.recruitment.events.ScoresReadyEvent;
import reactor.core.publisher.Mono;

public interface EvaluationEventPublisherPort {
    Mono<Void> publishScoresReady(ScoresReadyEvent event);
}
