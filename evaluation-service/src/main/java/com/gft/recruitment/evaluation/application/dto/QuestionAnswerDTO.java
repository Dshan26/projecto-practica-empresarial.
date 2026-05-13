package com.gft.recruitment.evaluation.application.dto;

import java.util.UUID;

public record QuestionAnswerDTO(
        UUID questionId,
        String respuestaCorrecta
) {}
