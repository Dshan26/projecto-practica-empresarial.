package com.gft.recruitment.ai.application.dto;

import java.util.List;

public record OpenAiAnalysisResponse(
        List<SkillDto> habilidadesTecnicas,
        int aniosExperiencia,
        String seniorityDetectado,
        String resumenProfesional,
        String rawResponse
) {
    public record SkillDto(String nombre, String nivel) {
    }
}
