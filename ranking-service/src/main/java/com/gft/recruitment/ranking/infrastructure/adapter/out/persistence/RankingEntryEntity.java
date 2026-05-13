package com.gft.recruitment.ranking.infrastructure.adapter.out.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("ranking_entries")
public class RankingEntryEntity implements Persistable<UUID> {

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

    @Column("tech_score")
    private int techScore;

    @Column("final_score")
    private double finalScore;

    @Column("ranking_position")
    private Integer rankingPosition;

    @Column("calculated_at")
    private LocalDateTime calculatedAt;

    public RankingEntryEntity() {
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

    public int getTechScore() { return techScore; }
    public void setTechScore(int techScore) { this.techScore = techScore; }

    public double getFinalScore() { return finalScore; }
    public void setFinalScore(double finalScore) { this.finalScore = finalScore; }

    public Integer getRankingPosition() { return rankingPosition; }
    public void setRankingPosition(Integer rankingPosition) { this.rankingPosition = rankingPosition; }

    public LocalDateTime getCalculatedAt() { return calculatedAt; }
    public void setCalculatedAt(LocalDateTime calculatedAt) { this.calculatedAt = calculatedAt; }

    @Override
    @Transient
    public boolean isNew() { return isNew; }
    public void setNew(boolean isNew) { this.isNew = isNew; }
}
