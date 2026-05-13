package com.gft.recruitment.ranking.domain.port.in;

import com.gft.recruitment.ranking.application.dto.RankingDetailResponse;
import com.gft.recruitment.ranking.application.dto.RankingEntryResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface GetRankingUseCase {
    Flux<RankingEntryResponse> getRanking(UUID jobOfferId);
    Mono<RankingDetailResponse> getDetail(UUID jobOfferId, UUID candidateId);
}
