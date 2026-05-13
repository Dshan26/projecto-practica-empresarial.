package com.gft.recruitment.ai.domain.port.out;

import com.gft.recruitment.ai.application.dto.OpenAiAnalysisResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface OpenAiPort {
    Mono<OpenAiAnalysisResponse> analyzeCv(String cvText, List<String> requiredSkills, String seniorityExpected);
}
