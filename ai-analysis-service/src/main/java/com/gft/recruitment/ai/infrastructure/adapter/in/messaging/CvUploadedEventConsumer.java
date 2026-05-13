package com.gft.recruitment.ai.infrastructure.adapter.in.messaging;

import com.gft.recruitment.ai.domain.port.in.AnalyzeCvUseCase;
import com.gft.recruitment.events.CvUploadedEvent;
import com.gft.recruitment.events.KafkaTopics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CvUploadedEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(CvUploadedEventConsumer.class);

    private final AnalyzeCvUseCase analyzeCvUseCase;

    public CvUploadedEventConsumer(AnalyzeCvUseCase analyzeCvUseCase) {
        this.analyzeCvUseCase = analyzeCvUseCase;
    }

    @KafkaListener(topics = KafkaTopics.CV_UPLOADED, groupId = "${spring.kafka.consumer.group-id:ai-analysis-group}")
    public void onCvUploaded(CvUploadedEvent event) {
        log.info("Received CV_UPLOADED event: cvId={}, candidateId={}, jobOfferId={}",
                event.cvId(), event.candidateId(), event.jobOfferId());

        analyzeCvUseCase.analyze(event)
                .doOnSuccess(result -> log.info("CV analysis completed for cvId={}, status={}",
                        event.cvId(), result.getStatus()))
                .doOnError(error -> log.error("CV analysis failed for cvId={}: {}",
                        event.cvId(), error.getMessage()))
                .subscribe();
    }
}
