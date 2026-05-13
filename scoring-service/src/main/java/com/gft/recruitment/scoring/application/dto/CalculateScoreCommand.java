package com.gft.recruitment.scoring.application.dto;

import java.util.List;
import java.util.UUID;

public record CalculateScoreCommand(
    UUID candidateId,
    UUID jobOfferId,
    List<String> identifiedSkills,
    int yearsOfExperience,
    String detectedSeniority,
    List<String> requiredSkills,
    String requiredSeniority,
    boolean analysisError
) {}
