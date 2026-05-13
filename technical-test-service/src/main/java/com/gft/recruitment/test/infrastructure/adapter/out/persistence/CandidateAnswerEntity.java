package com.gft.recruitment.test.infrastructure.adapter.out.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("candidate_answers")
public class CandidateAnswerEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Transient
    private boolean isNew = true;

    @Column("test_id")
    private UUID testId;

    @Column("candidate_id")
    private UUID candidateId;

    @Column("job_offer_id")
    private UUID jobOfferId;

    @Column("question_id")
    private UUID questionId;

    @Column("respuesta")
    private String respuesta;

    @Column("submitted_at")
    private LocalDateTime submittedAt;

    public CandidateAnswerEntity() {
    }

    @Override
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getTestId() { return testId; }
    public void setTestId(UUID testId) { this.testId = testId; }

    public UUID getCandidateId() { return candidateId; }
    public void setCandidateId(UUID candidateId) { this.candidateId = candidateId; }

    public UUID getJobOfferId() { return jobOfferId; }
    public void setJobOfferId(UUID jobOfferId) { this.jobOfferId = jobOfferId; }

    public UUID getQuestionId() { return questionId; }
    public void setQuestionId(UUID questionId) { this.questionId = questionId; }

    public String getRespuesta() { return respuesta; }
    public void setRespuesta(String respuesta) { this.respuesta = respuesta; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    @Override
    @Transient
    public boolean isNew() { return isNew; }
    public void setNew(boolean isNew) { this.isNew = isNew; }
}
