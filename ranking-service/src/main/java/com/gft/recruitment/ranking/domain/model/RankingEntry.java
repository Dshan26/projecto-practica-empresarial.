package com.gft.recruitment.ranking.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class RankingEntry {

    private UUID id;
    private UUID candidateId;
    private UUID jobOfferId;
    private int cvScore;
    private int techScore;
    private double finalScore;
    private Integer rankingPosition;
    private LocalDateTime calculatedAt;

    public RankingEntry() {
    }

    public RankingEntry(UUID id, UUID candidateId, UUID jobOfferId, int cvScore, int techScore,
                        double finalScore, Integer rankingPosition, LocalDateTime calculatedAt) {
        this.id = id;
        this.candidateId = candidateId;
        this.jobOfferId = jobOfferId;
        this.cvScore = cvScore;
        this.techScore = techScore;
        this.finalScore = finalScore;
        this.rankingPosition = rankingPosition;
        this.calculatedAt = calculatedAt;
    }

    /**
     * Calculates the final score using the weighted formula:
     * Final_Score = (0.6 * CV_Score) + (0.4 * Tech_Score)
     */
    public static double calculateFinalScore(int cvScore, int techScore) {
        return (0.6 * cvScore) + (0.4 * techScore);
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getCandidateId() { return candidateId; }
    public void setCandidateId(UUID candidateId) { this.candidateId = candidateId; }

    public UUID getJobOfferId() { return jobOfferId; }
    public void setJobOfferId(UUID jobOfferId) { this.jobOfferId = jobOfferId; }

    public int getCvScore() { return cvScore; }
    public void setCvScore(int cvScore) { this.cvScore = cvScore; }

    public int getTechScore() { return techScore; }
    public void setTechScore(int techScore) { this.techScore = techScore; }

    public double getFinalScore() { return finalScore; }
    public void setFinalScore(double finalScore) { this.finalScore = finalScore; }

    public Integer getRankingPosition() { return rankingPosition; }
    public void setRankingPosition(Integer rankingPosition) { this.rankingPosition = rankingPosition; }

    public LocalDateTime getCalculatedAt() { return calculatedAt; }
    public void setCalculatedAt(LocalDateTime calculatedAt) { this.calculatedAt = calculatedAt; }
}
