package com.gft.recruitment.evaluation.infrastructure.adapter.out.messaging;

import com.gft.recruitment.evaluation.domain.port.out.EvaluationEventPublisherPort;
import com.gft.recruitment.events.KafkaTopics;
import com.gft.recruitment.events.ScoresReadyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class KafkaEvaluationEventPublisher implements EvaluationEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaEvaluationEventPublisher.class);

    private final KafkaTemplate<String, ScoresReadyEvent> kafkaTemplate;

    public KafkaEvaluationEventPublisher(KafkaTemplate<String, ScoresReadyEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Mono<Void> publishScoresReady(ScoresReadyEvent event) {
        return Mono.fromFuture(
                kafkaTemplate.send(KafkaTopics.SCORES_READY,
                                event.jobOfferId().toString(), event)
                        .toCompletableFuture()
        ).doOnSuccess(result -> log.info(
                "Published SCORES_READY event for candidateId={}, jobOfferId={}, cvScore={}, techScore={}",
                event.candidateId(), event.jobOfferId(), event.cvScore(), event.techScore()))
         .doOnError(e -> log.error("Failed to publish SCORES_READY event: {}", e.getMessage()))
         .then();
    }
}
