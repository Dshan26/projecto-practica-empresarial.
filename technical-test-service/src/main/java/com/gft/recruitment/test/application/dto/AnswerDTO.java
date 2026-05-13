package com.gft.recruitment.test.application.dto;

import java.util.UUID;

public record AnswerDTO(
        UUID questionId,
        String respuesta
) {
}
