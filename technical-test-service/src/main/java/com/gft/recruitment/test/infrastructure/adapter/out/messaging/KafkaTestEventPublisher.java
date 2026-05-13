package com.gft.recruitment.test.infrastructure.adapter.out.messaging;

import com.gft.recruitment.events.KafkaTopics;
import com.gft.recruitment.events.TestCompletedEvent;
import com.gft.recruitment.test.domain.port.out.TestEventPublisherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class KafkaTestEventPublisher implements TestEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaTestEventPublisher.class);

    private final KafkaTemplate<String, TestCompletedEvent> kafkaTemplate;

    public KafkaTestEventPublisher(KafkaTemplate<String, TestCompletedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Mono<Void> publishTestCompleted(TestCompletedEvent event) {
        return Mono.fromFuture(
                kafkaTemplate.send(KafkaTopics.TEST_COMPLETED, event.jobOfferId().toString(), event)
                        .toCompletableFuture()
        ).doOnSuccess(result -> log.info("Published TEST_COMPLETED event for testId={}, candidateId={}, jobOfferId={}",
                event.testId(), event.candidateId(), event.jobOfferId()))
         .doOnError(e -> log.error("Failed to publish TEST_COMPLETED event: {}", e.getMessage()))
         .then();
    }
}
