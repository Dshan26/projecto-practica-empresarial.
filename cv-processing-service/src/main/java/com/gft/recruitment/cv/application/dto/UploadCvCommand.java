package com.gft.recruitment.cv.application.dto;

import java.util.UUID;

public record UploadCvCommand(
    UUID candidateId,
    UUID jobOfferId,
    String fileName,
    long fileSize,
    byte[] fileContent
) {}
