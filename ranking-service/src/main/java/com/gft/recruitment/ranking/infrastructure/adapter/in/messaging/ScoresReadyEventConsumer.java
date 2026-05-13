package com.gft.recruitment.ranking.infrastructure.adapter.in.messaging;

import com.gft.recruitment.events.KafkaTopics;
import com.gft.recruitment.events.ScoresReadyEvent;
import com.gft.recruitment.ranking.domain.port.in.CalculateRankingUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ScoresReadyEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(ScoresReadyEventConsumer.class);

    private final CalculateRankingUseCase calculateRankingUseCase;

    public ScoresReadyEventConsumer(CalculateRankingUseCase calculateRankingUseCase) {
        this.calculateRankingUseCase = calculateRankingUseCase;
    }

    @KafkaListener(topics = KafkaTopics.SCORES_READY,
                   groupId = "${spring.kafka.consumer.group-id:ranking-group}")
    public void onScoresReady(ScoresReadyEvent event) {
        log.info("Received SCORES_READY event: candidateId={}, jobOfferId={}, cvScore={}, techScore={}",
                event.candidateId(), event.jobOfferId(), event.cvScore(), event.techScore());

        calculateRankingUseCase.calculate(event)
                .doOnSuccess(entry -> log.info(
                        "Ranking calculated for candidateId={}, finalScore={}, position={}",
                        event.candidateId(), entry.getFinalScore(), entry.getRankingPosition()))
                .doOnError(error -> log.error(
                        "Ranking calculation failed for candidateId={}: {}",
                        event.candidateId(), error.getMessage()))
                .subscribe();
    }
}
