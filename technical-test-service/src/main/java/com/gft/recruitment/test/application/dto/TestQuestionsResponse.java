package com.gft.recruitment.test.application.dto;

import java.util.List;
import java.util.UUID;

public record TestQuestionsResponse(
        UUID testId,
        UUID jobOfferId,
        List<QuestionDTO> preguntas
) {
}
