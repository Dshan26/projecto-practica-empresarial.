package com.gft.recruitment.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.UUID;

/**
 * Event published when both CV and technical test scores are available.
 * Topic: SCORES_READY
 */
public record ScoresReadyEvent(
        @JsonProperty("eventId") UUID eventId,
        @JsonFormat(shape = JsonFormat.Shape.STRING) @JsonProperty("timestamp") Instant timestamp,
        @JsonProperty("candidateId") UUID candidateId,
        @JsonProperty("jobOfferId") UUID jobOfferId,
        @JsonProperty("cvScore") int cvScore,
        @JsonProperty("techScore") int techScore
) {
}
