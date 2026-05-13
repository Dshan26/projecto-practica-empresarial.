package com.gft.recruitment.test.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class TechnicalTest {

    private UUID id;
    private UUID jobOfferId;
    private LocalDateTime createdAt;

    public TechnicalTest() {
    }

    public TechnicalTest(UUID id, UUID jobOfferId, LocalDateTime createdAt) {
        this.id = id;
        this.jobOfferId = jobOfferId;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getJobOfferId() { return jobOfferId; }
    public void setJobOfferId(UUID jobOfferId) { this.jobOfferId = jobOfferId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
