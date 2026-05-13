package com.gft.recruitment.evaluation.infrastructure.adapter.out.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("evaluation_results")
public class EvaluationResultEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Transient
    private boolean isNew = true;

    @Column("candidate_id")
    private UUID candidateId;

    @Column("job_offer_id")
    private UUID jobOfferId;

    @Column("test_id")
    private UUID testId;

    @Column("tech_score")
    private int techScore;

    @Column("total_questions")
    private int totalQuestions;

    @Column("correct_answers")
    private int correctAnswers;

    @Column("evaluated_at")
    private LocalDateTime evaluatedAt;

    public EvaluationResultEntity() {
    }

    @Override
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

    @Override
    @Transient
    public boolean isNew() { return isNew; }
    public void setNew(boolean isNew) { this.isNew = isNew; }
}
