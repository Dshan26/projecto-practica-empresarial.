package com.gft.recruitment.ranking.application.dto;

import java.util.UUID;

public record RankingEntryResponse(
        UUID candidateId,
        String candidateName,
        int cvScore,
        int techScore,
        double finalScore,
        Integer rankingPosition
) {
}
