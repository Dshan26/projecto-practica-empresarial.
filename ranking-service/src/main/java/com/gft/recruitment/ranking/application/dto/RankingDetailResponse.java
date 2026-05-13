package com.gft.recruitment.ranking.application.dto;

import java.util.List;
import java.util.UUID;

public record RankingDetailResponse(
        UUID candidateId,
        String candidateName,
        int cvScore,
        int techScore,
        double finalScore,
        Integer rankingPosition,
        List<String> habilidades,
        String resumenProfesional
) {
}
