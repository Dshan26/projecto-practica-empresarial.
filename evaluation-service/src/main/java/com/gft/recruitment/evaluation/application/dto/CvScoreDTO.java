package com.gft.recruitment.evaluation.application.dto;

import java.util.UUID;

public record CvScoreDTO(
        UUID candidateId,
        UUID jobOfferId,
        int cvScore
) {}
