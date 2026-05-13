package com.gft.recruitment.test.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class TestSubmission {

    private UUID id;
    private UUID testId;
    private UUID candidateId;
    private UUID jobOfferId;
    private LocalDateTime submittedAt;

    public TestSubmission() {
    }

    public TestSubmission(UUID id, UUID testId, UUID candidateId, UUID jobOfferId, LocalDateTime submittedAt) {
        this.id = id;
        this.testId = testId;
        this.candidateId = candidateId;
        this.jobOfferId = jobOfferId;
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

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}
