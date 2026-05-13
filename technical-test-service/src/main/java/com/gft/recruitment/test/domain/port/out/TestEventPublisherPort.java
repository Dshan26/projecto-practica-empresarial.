package com.gft.recruitment.test.domain.port.out;

import com.gft.recruitment.events.TestCompletedEvent;
import reactor.core.publisher.Mono;

public interface TestEventPublisherPort {
    Mono<Void> publishTestCompleted(TestCompletedEvent event);
}
