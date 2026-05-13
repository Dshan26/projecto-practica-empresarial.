package com.gft.recruitment.evaluation.infrastructure.adapter.in.messaging;

import com.gft.recruitment.evaluation.domain.port.in.EvaluateTestUseCase;
import com.gft.recruitment.events.KafkaTopics;
import com.gft.recruitment.events.TestCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TestCompletedEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(TestCompletedEventConsumer.class);

    private final EvaluateTestUseCase evaluateTestUseCase;

    public TestCompletedEventConsumer(EvaluateTestUseCase evaluateTestUseCase) {
        this.evaluateTestUseCase = evaluateTestUseCase;
    }

    @KafkaListener(topics = KafkaTopics.TEST_COMPLETED,
                   groupId = "${spring.kafka.consumer.group-id:evaluation-group}")
    public void onTestCompleted(TestCompletedEvent event) {
        log.info("Received TEST_COMPLETED event: testId={}, candidateId={}, jobOfferId={}",
                event.testId(), event.candidateId(), event.jobOfferId());

        evaluateTestUseCase.evaluate(event)
                .doOnSuccess(result -> log.info(
                        "Evaluation completed for candidateId={}, techScore={}",
                        event.candidateId(), result.getTechScore()))
                .doOnError(error -> log.error(
                        "Evaluation failed for candidateId={}: {}",
                        event.candidateId(), error.getMessage()))
                .subscribe();
    }
}
