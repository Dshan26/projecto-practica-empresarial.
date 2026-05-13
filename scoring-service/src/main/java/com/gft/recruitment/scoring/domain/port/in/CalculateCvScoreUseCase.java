package com.gft.recruitment.scoring.domain.port.in;

import com.gft.recruitment.scoring.application.dto.CalculateScoreCommand;
import com.gft.recruitment.scoring.domain.model.CvScore;
import reactor.core.publisher.Mono;

public interface CalculateCvScoreUseCase {
    Mono<CvScore> calculate(CalculateScoreCommand command);
}
