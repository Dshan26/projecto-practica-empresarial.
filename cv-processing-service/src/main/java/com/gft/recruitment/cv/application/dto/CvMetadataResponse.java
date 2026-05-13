package com.gft.recruitment.cv.application.dto;

import com.gft.recruitment.cv.domain.model.CvStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record CvMetadataResponse(
    UUID id,
    UUID candidateId,
    UUID jobOfferId,
    String fileName,
    long fileSize,
    CvStatus status,
    LocalDateTime uploadedAt
) {}
