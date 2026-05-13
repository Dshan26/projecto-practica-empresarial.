package com.gft.recruitment.ranking.infrastructure.adapter.in.web;

import com.gft.recruitment.ranking.application.dto.RankingDetailResponse;
import com.gft.recruitment.ranking.application.dto.RankingEntryResponse;
import com.gft.recruitment.ranking.domain.port.in.GetRankingUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rankings")
public class RankingController {

    private final GetRankingUseCase getRankingUseCase;

    public RankingController(GetRankingUseCase getRankingUseCase) {
        this.getRankingUseCase = getRankingUseCase;
    }

    @GetMapping("/{jobOfferId}")
    public Mono<ResponseEntity<List<RankingEntryResponse>>> getRanking(@PathVariable UUID jobOfferId) {
        return getRankingUseCase.getRanking(jobOfferId)
                .collectList()
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{jobOfferId}/candidates/{candidateId}")
    public Mono<ResponseEntity<RankingDetailResponse>> getDetail(
            @PathVariable UUID jobOfferId,
            @PathVariable UUID candidateId) {
        return getRankingUseCase.getDetail(jobOfferId, candidateId)
                .map(ResponseEntity::ok);
    }
}
