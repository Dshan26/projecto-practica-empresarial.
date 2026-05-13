package com.gft.recruitment.evaluation.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class EvaluationResult {

    private UUID id;
    private UUID candidateId;
    private UUID jobOfferId;
    private UUID testId;
    private int techScore;
    private int totalQuestions;
    private int correctAnswers;
    private LocalDateTime evaluatedAt;

    public EvaluationResult() {
    }

    public EvaluationResult(UUID id, UUID candidateId, UUID jobOfferId, UUID testId,
                            int techScore, int totalQuestions, int correctAnswers,
                            LocalDateTime evaluatedAt) {
        this.id = id;
        this.candidateId = candidateId;
        this.jobOfferId = jobOfferId;
        this.testId = testId;
        this.techScore = techScore;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.evaluatedAt = evaluatedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getCandidateId() { return candidateId; }
    public void setCandidateId(UUID candidateId) { this.candidateId = candidateId; }

    public UUID getJobOfferId() { return jobOfferId; }
    public void setJobOfferId(UUID jobOfferId) { this.jobOfferId = jobOfferId; }

    public UUID getTestId() { return testId; }
    public void setTestId(UUID testId) { this.testId = testId; }

    public int getTechScore() { return techScore; }
    public void setTechScore(int techScore) { this.techScore = techScore; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public int getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(int correctAnswers) { this.correctAnswers = correctAnswers; }

    public LocalDateTime getEvaluatedAt() { return evaluatedAt; }
    public void setEvaluatedAt(LocalDateTime evaluatedAt) { this.evaluatedAt = evaluatedAt; }
}
