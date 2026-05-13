package com.gft.recruitment.ranking.domain.port.in;

import com.gft.recruitment.events.ScoresReadyEvent;
import com.gft.recruitment.ranking.domain.model.RankingEntry;
import reactor.core.publisher.Mono;

public interface CalculateRankingUseCase {
    Mono<RankingEntry> calculate(ScoresReadyEvent event);
}
