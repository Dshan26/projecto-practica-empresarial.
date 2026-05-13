package com.gft.recruitment.test.application.dto;

import java.util.List;
import java.util.UUID;

public record SubmitAnswersCommand(
        UUID testId,
        UUID candidateId,
        UUID jobOfferId,
        List<AnswerDTO> respuestas
) {
}
