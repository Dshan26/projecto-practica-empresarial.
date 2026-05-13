package com.gft.recruitment.evaluation.application.dto;

import java.util.UUID;

public record CandidateAnswerDTO(
        UUID questionId,
        String respuesta
) {}
