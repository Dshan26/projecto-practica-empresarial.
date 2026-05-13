package com.gft.recruitment.ai.domain.port.in;

import com.gft.recruitment.ai.domain.model.AnalysisResult;
import com.gft.recruitment.events.CvUploadedEvent;
import reactor.core.publisher.Mono;

public interface AnalyzeCvUseCase {
    Mono<AnalysisResult> analyze(CvUploadedEvent event);
}
