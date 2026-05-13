package com.gft.recruitment.scoring.infrastructure.adapter.out.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("cv_scores")
public class CvScoreEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Transient
    private boolean isNew = true;

    @Column("candidate_id")
    private UUID candidateId;

    @Column("job_offer_id")
    private UUID jobOfferId;

    @Column("cv_score")
    private int cvScore;

    @Column("score_details")
    private String scoreDetails;

    @Column("analysis_error")
    private boolean analysisError;

    @Column("calculated_at")
    private LocalDateTime calculatedAt;

    public CvScoreEntity() {
    }

    @Override
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getCandidateId() { return candidateId; }
    public void setCandidateId(UUID candidateId) { this.candidateId = candidateId; }

    public UUID getJobOfferId() { return jobOfferId; }
    public void setJobOfferId(UUID jobOfferId) { this.jobOfferId = jobOfferId; }

    public int getCvScore() { return cvScore; }
    public void setCvScore(int cvScore) { this.cvScore = cvScore; }

    public String getScoreDetails() { return scoreDetails; }
    public void setScoreDetails(String scoreDetails) { this.scoreDetails = scoreDetails; }

    public boolean isAnalysisError() { return analysisError; }
    public void setAnalysisError(boolean analysisError) { this.analysisError = analysisError; }

    public LocalDateTime getCalculatedAt() { return calculatedAt; }
    public void setCalculatedAt(LocalDateTime calculatedAt) { this.calculatedAt = calculatedAt; }

    @Override
    @Transient
    public boolean isNew() { return isNew; }
    public void setNew(boolean isNew) { this.isNew = isNew; }
}
