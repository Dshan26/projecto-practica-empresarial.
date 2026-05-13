package com.gft.recruitment.test.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class CandidateAnswer {

    private UUID id;
    private UUID testId;
    private UUID candidateId;
    private UUID jobOfferId;
    private UUID questionId;
    private String respuesta;
    private LocalDateTime submittedAt;

    public CandidateAnswer() {
    }

    public CandidateAnswer(UUID id, UUID testId, UUID candidateId, UUID jobOfferId,
                           UUID questionId, String respuesta, LocalDateTime submittedAt) {
        this.id = id;
        this.testId = testId;
        this.candidateId = candidateId;
        this.jobOfferId = jobOfferId;
        this.questionId = questionId;
        this.respuesta = respuesta;
        this.submittedAt = submittedAt;
    }

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
}
