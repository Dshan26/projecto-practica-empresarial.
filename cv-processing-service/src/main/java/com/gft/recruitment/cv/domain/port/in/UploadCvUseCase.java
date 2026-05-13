package com.gft.recruitment.cv.domain.port.in;

import com.gft.recruitment.cv.application.dto.CvMetadataResponse;
import com.gft.recruitment.cv.application.dto.UploadCvCommand;
import reactor.core.publisher.Mono;

public interface UploadCvUseCase {
    Mono<CvMetadataResponse> upload(UploadCvCommand command);
}
