package com.gft.recruitment.cv.infrastructure.adapter.out.messaging;

import com.gft.recruitment.cv.domain.port.out.CvEventPublisherPort;
import com.gft.recruitment.events.CvUploadedEvent;
import com.gft.recruitment.events.KafkaTopics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class KafkaCvEventPublisher implements CvEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaCvEventPublisher.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaCvEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Mono<Void> publishCvUploaded(CvUploadedEvent event) {
        return Mono.fromFuture(() -> kafkaTemplate.send(
                        KafkaTopics.CV_UPLOADED,
                        event.cvId().toString(),
                        event
                ).toCompletableFuture())
                .doOnSuccess(result -> log.info("Published CV_UPLOADED event for cvId: {}", event.cvId()))
                .doOnError(error -> log.error("Failed to publish CV_UPLOADED event for cvId: {}", event.cvId(), error))
                .then();
    }
}
