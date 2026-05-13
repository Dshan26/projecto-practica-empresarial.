package com.gft.recruitment.scoring.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class CvScore {

    private UUID id;
    private UUID candidateId;
    private UUID jobOfferId;
    private int cvScore;
    private ScoreDetails details;
    private boolean analysisError;
    private LocalDateTime calculatedAt;

    public CvScore() {
    }

    public CvScore(UUID id, UUID candidateId, UUID jobOfferId, int cvScore,
                   ScoreDetails details, boolean analysisError, LocalDateTime calculatedAt) {
        this.id = id;
        this.candidateId = candidateId;
        this.jobOfferId = jobOfferId;
        this.cvScore = cvScore;
        this.details = details;
        this.analysisError = analysisError;
        this.calculatedAt = calculatedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getCandidateId() { return candidateId; }
    public void setCandidateId(UUID candidateId) { this.candidateId = candidateId; }

    public UUID getJobOfferId() { return jobOfferId; }
    public void setJobOfferId(UUID jobOfferId) { this.jobOfferId = jobOfferId; }

    public int getCvScore() { return cvScore; }
    public void setCvScore(int cvScore) { this.cvScore = cvScore; }

    public ScoreDetails getDetails() { return details; }
    public void setDetails(ScoreDetails details) { this.details = details; }

    public boolean isAnalysisError() { return analysisError; }
    public void setAnalysisError(boolean analysisError) { this.analysisError = analysisError; }

    public LocalDateTime getCalculatedAt() { return calculatedAt; }
    public void setCalculatedAt(LocalDateTime calculatedAt) { this.calculatedAt = calculatedAt; }
}
