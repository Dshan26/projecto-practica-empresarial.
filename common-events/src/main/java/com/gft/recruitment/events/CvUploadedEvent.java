package com.gft.recruitment.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.UUID;

/**
 * Event published when a CV is successfully uploaded and stored.
 * Topic: CV_UPLOADED
 */
public record CvUploadedEvent(
        @JsonProperty("eventId") UUID eventId,
        @JsonFormat(shape = JsonFormat.Shape.STRING) @JsonProperty("timestamp") Instant timestamp,
        @JsonProperty("cvId") UUID cvId,
        @JsonProperty("candidateId") UUID candidateId,
        @JsonProperty("jobOfferId") UUID jobOfferId,
        @JsonProperty("fileName") String fileName,
        @JsonProperty("fileSize") long fileSize
) {
}
