package com.gft.recruitment.scoring.application.service;

import com.gft.recruitment.scoring.application.dto.CalculateScoreCommand;
import com.gft.recruitment.scoring.domain.model.CvScore;
import com.gft.recruitment.scoring.domain.model.ScoreDetails;
import com.gft.recruitment.scoring.domain.port.in.CalculateCvScoreUseCase;
import com.gft.recruitment.scoring.domain.port.out.CvScoreRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ScoringService implements CalculateCvScoreUseCase {

    private static final Logger log = LoggerFactory.getLogger(ScoringService.class);

    private final CvScoreRepositoryPort cvScoreRepository;

    @Value("${scoring.weights.skills:0.50}")
    private double skillWeight;

    @Value("${scoring.weights.experience:0.30}")
    private double experienceWeight;

    @Value("${scoring.weights.seniority:0.20}")
    private double seniorityWeight;

    public ScoringService(CvScoreRepositoryPort cvScoreRepository) {
        this.cvScoreRepository = cvScoreRepository;
    }

    @Override
    public Mono<CvScore> calculate(CalculateScoreCommand command) {
        return Mono.defer(() -> {
            log.info("Calculating CV score for candidateId={}, jobOfferId={}",
                    command.candidateId(), command.jobOfferId());

            CvScore cvScore = new CvScore();
            cvScore.setId(UUID.randomUUID());
            cvScore.setCandidateId(command.candidateId());
            cvScore.setJobOfferId(command.jobOfferId());
            cvScore.setCalculatedAt(LocalDateTime.now());

            if (command.analysisError()) {
                log.warn("Analysis error detected, assigning score 0");
                cvScore.setCvScore(0);
                cvScore.setAnalysisError(true);
                cvScore.setDetails(new ScoreDetails(0, 0, 0));
                return cvScoreRepository.save(cvScore);
            }

            double skillMatch = calculateSkillMatch(
                    command.identifiedSkills(), command.requiredSkills());
            double experienceScore = calculateExperienceScore(
                    command.yearsOfExperience());
            double seniorityScore = calculateSeniorityScore(
                    command.detectedSeniority(), command.requiredSeniority());

            double rawScore = (skillMatch * skillWeight)
                    + (experienceScore * experienceWeight)
                    + (seniorityScore * seniorityWeight);

            int finalScore = clampScore(rawScore);

            cvScore.setCvScore(finalScore);
            cvScore.setAnalysisError(false);
            cvScore.setDetails(new ScoreDetails(skillMatch, experienceScore, seniorityScore));

            log.info("CV score calculated: {} (skills={}, exp={}, seniority={})",
                    finalScore, skillMatch, experienceScore, seniorityScore);

            return cvScoreRepository.save(cvScore);
        });
    }

    double calculateSkillMatch(List<String> identified, List<String> required) {
        if (required == null || required.isEmpty()) {
            return 100.0;
        }
        if (identified == null || identified.isEmpty()) {
            return 0.0;
        }
        List<String> normalizedIdentified = identified.stream()
                .map(String::toLowerCase).map(String::trim).toList();
        long matched = required.stream()
                .map(String::toLowerCase).map(String::trim)
                .filter(normalizedIdentified::contains)
                .count();
        return (matched * 100.0) / required.size();
    }

    double calculateExperienceScore(int yearsOfExperience) {
        if (yearsOfExperience <= 0) return 0.0;
        if (yearsOfExperience >= 10) return 100.0;
        return (yearsOfExperience * 100.0) / 10.0;
    }

    double calculateSeniorityScore(String detected, String required) {
        int detectedLevel = seniorityToLevel(detected);
        int requiredLevel = seniorityToLevel(required);
        if (detectedLevel >= requiredLevel) {
            return 100.0;
        }
        if (requiredLevel == 0) return 100.0;
        return (detectedLevel * 100.0) / requiredLevel;
    }

    private int seniorityToLevel(String seniority) {
        if (seniority == null) return 0;
        return switch (seniority.toUpperCase()) {
            case "JUNIOR" -> 1;
            case "SEMI_SENIOR" -> 2;
            case "SENIOR" -> 3;
            default -> 0;
        };
    }

    private int clampScore(double score) {
        return (int) Math.max(0, Math.min(100, Math.round(score)));
    }
}
