package com.gft.recruitment.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.UUID;

/**
 * Event published when a candidate completes a technical test.
 * Topic: TEST_COMPLETED
 */
public record TestCompletedEvent(
        @JsonProperty("eventId") UUID eventId,
        @JsonFormat(shape = JsonFormat.Shape.STRING) @JsonProperty("timestamp") Instant timestamp,
        @JsonProperty("testId") UUID testId,
        @JsonProperty("candidateId") UUID candidateId,
        @JsonProperty("jobOfferId") UUID jobOfferId
) {
}
