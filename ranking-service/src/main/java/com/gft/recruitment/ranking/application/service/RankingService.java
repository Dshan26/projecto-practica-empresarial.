package com.gft.recruitment.ranking.application.service;

import com.gft.recruitment.events.ScoresReadyEvent;
import com.gft.recruitment.ranking.application.dto.RankingDetailResponse;
import com.gft.recruitment.ranking.application.dto.RankingEntryResponse;
import com.gft.recruitment.ranking.domain.model.RankingEntry;
import com.gft.recruitment.ranking.domain.port.in.CalculateRankingUseCase;
import com.gft.recruitment.ranking.domain.port.in.GetRankingUseCase;
import com.gft.recruitment.ranking.domain.port.out.RankingRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class RankingService implements CalculateRankingUseCase, GetRankingUseCase {

    private static final Logger log = LoggerFactory.getLogger(RankingService.class);

    private final RankingRepositoryPort rankingRepository;
    private final WebClient serviceWebClient;

    public RankingService(RankingRepositoryPort rankingRepository,
                          @Qualifier("serviceWebClient") WebClient serviceWebClient) {
        this.rankingRepository = rankingRepository;
        this.serviceWebClient = serviceWebClient;
    }

    @Override
    public Mono<RankingEntry> calculate(ScoresReadyEvent event) {
        double finalScore = RankingEntry.calculateFinalScore(event.cvScore(), event.techScore());

        RankingEntry entry = new RankingEntry();
        entry.setId(UUID.randomUUID());
        entry.setCandidateId(event.candidateId());
        entry.setJobOfferId(event.jobOfferId());
        entry.setCvScore(event.cvScore());
        entry.setTechScore(event.techScore());
        entry.setFinalScore(finalScore);
        entry.setCalculatedAt(LocalDateTime.now());

        return rankingRepository.save(entry)
                .flatMap(saved -> rankingRepository.updateRankingPositions(saved.getJobOfferId())
                        .then(rankingRepository.findByCandidateAndJobOffer(
                                saved.getCandidateId(), saved.getJobOfferId())));
    }

    @Override
    public Flux<RankingEntryResponse> getRanking(UUID jobOfferId) {
        return rankingRepository.findByJobOfferIdOrderByFinalScoreDesc(jobOfferId)
                .flatMapSequential(entry -> fetchCandidateName(entry.getCandidateId())
                        .map(name -> new RankingEntryResponse(
                                entry.getCandidateId(),
                                name,
                                entry.getCvScore(),
                                entry.getTechScore(),
                                entry.getFinalScore(),
                                entry.getRankingPosition()
                        )));
    }

    @Override
    public Mono<RankingDetailResponse> getDetail(UUID jobOfferId, UUID candidateId) {
        return rankingRepository.findByCandidateAndJobOffer(candidateId, jobOfferId)
                .switchIfEmpty(Mono.error(new RuntimeException(
                        "Ranking entry not found for candidateId=" + candidateId + ", jobOfferId=" + jobOfferId)))
                .flatMap(entry -> {
                    Mono<String> nameMono = fetchCandidateName(entry.getCandidateId());
                    Mono<Map> analysisMono = fetchAnalysis(entry.getCandidateId(), entry.getJobOfferId());

                    return Mono.zip(nameMono, analysisMono)
                            .map(tuple -> {
                                String name = tuple.getT1();
                                Map analysis = tuple.getT2();

                                @SuppressWarnings("unchecked")
                                List<String> habilidades = analysis.get("habilidades") != null
                                        ? (List<String>) analysis.get("habilidades")
                                        : Collections.emptyList();
                                String resumen = analysis.get("resumenProfesional") != null
                                        ? analysis.get("resumenProfesional").toString()
                                        : "";

                                return new RankingDetailResponse(
                                        entry.getCandidateId(),
                                        name,
                                        entry.getCvScore(),
                                        entry.getTechScore(),
                                        entry.getFinalScore(),
                                        entry.getRankingPosition(),
                                        habilidades,
                                        resumen
                                );
                            });
                });
    }

    /**
     * Fetches candidate name from user-service via Eureka-discovered WebClient.
     */
    private Mono<String> fetchCandidateName(UUID candidateId) {
        return serviceWebClient.get()
                .uri("http://user-service/api/users/{id}", candidateId)
                .retrieve()
                .bodyToMono(Map.class)
                .map(user -> user.get("nombre") != null ? user.get("nombre").toString() : "Unknown")
                .onErrorResume(e -> {
                    log.warn("Failed to fetch candidate name for id={}: {}", candidateId, e.getMessage());
                    return Mono.just("Unknown");
                });
    }

    /**
     * Fetches analysis result from ai-analysis-service via Eureka-discovered WebClient.
     */
    private Mono<Map> fetchAnalysis(UUID candidateId, UUID jobOfferId) {
        return serviceWebClient.get()
                .uri("http://ai-analysis-service/api/analysis/{candidateId}/{jobOfferId}", candidateId, jobOfferId)
                .retrieve()
                .bodyToMono(Map.class)
                .onErrorResume(e -> {
                    log.warn("Failed to fetch analysis for candidateId={}, jobOfferId={}: {}",
                            candidateId, jobOfferId, e.getMessage());
                    return Mono.just(Collections.emptyMap());
                });
    }
}
