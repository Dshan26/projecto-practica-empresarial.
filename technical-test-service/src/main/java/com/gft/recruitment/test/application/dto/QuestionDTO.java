package com.gft.recruitment.test.application.dto;

import com.gft.recruitment.test.domain.model.QuestionType;

import java.util.List;
import java.util.UUID;

public record QuestionDTO(
        UUID questionId,
        String enunciado,
        QuestionType tipo,
        List<String> opciones
) {
}
