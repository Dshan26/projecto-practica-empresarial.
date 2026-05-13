package com.gft.recruitment.scoring.domain.model;

public record ScoreDetails(
    double skillMatchPercentage,
    double experienceScore,
    double seniorityScore
) {}
