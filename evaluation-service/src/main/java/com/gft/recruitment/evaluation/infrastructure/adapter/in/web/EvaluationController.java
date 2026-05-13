package com.gft.recruitment.evaluation.infrastructure.adapter.in.web;

import com.gft.recruitment.evaluation.domain.model.EvaluationResult;
import com.gft.recruitment.evaluation.domain.port.out.EvaluationRepositoryPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/results")
public class EvaluationController {

    private final EvaluationRepositoryPort evaluationRepository;

    public EvaluationController(EvaluationRepositoryPort evaluationRepository) {
        this.evaluationRepository = evaluationRepository;
    }

    @GetMapping("/{candidateId}/{jobOfferId}")
    public Mono<EvaluationResult> getResult(
            @PathVariable UUID candidateId,
            @PathVariable UUID jobOfferId) {
        return evaluationRepository.findByCandidateAndJobOffer(candidateId, jobOfferId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Evaluation result not found for candidateId=" + candidateId
                                + " and jobOfferId=" + jobOfferId)));
    }
}
