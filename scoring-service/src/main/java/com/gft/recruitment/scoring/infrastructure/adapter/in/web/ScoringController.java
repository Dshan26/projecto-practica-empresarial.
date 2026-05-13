package com.gft.recruitment.scoring.infrastructure.adapter.in.web;

import com.gft.recruitment.scoring.application.dto.CalculateScoreCommand;
import com.gft.recruitment.scoring.domain.model.CvScore;
import com.gft.recruitment.scoring.domain.port.in.CalculateCvScoreUseCase;
import com.gft.recruitment.scoring.domain.port.out.CvScoreRepositoryPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/scores")
public class ScoringController {

    private final CalculateCvScoreUseCase calculateCvScoreUseCase;
    private final CvScoreRepositoryPort cvScoreRepositoryPort;

    public ScoringController(CalculateCvScoreUseCase calculateCvScoreUseCase,
                             CvScoreRepositoryPort cvScoreRepositoryPort) {
        this.calculateCvScoreUseCase = calculateCvScoreUseCase;
        this.cvScoreRepositoryPort = cvScoreRepositoryPort;
    }

    @PostMapping("/calculate")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CvScore> calculate(@RequestBody CalculateScoreCommand command) {
        return calculateCvScoreUseCase.calculate(command);
    }

    @GetMapping("/{candidateId}/{jobOfferId}")
    public Mono<Map<String, Object>> getCvScore(@PathVariable UUID candidateId, @PathVariable UUID jobOfferId) {
        return cvScoreRepositoryPort.findByCandidateAndJobOffer(candidateId, jobOfferId)
                .map(score -> Map.<String, Object>of("cvScore", score.getCvScore()))
                .defaultIfEmpty(Map.of("cvScore", 0));
    }
}
