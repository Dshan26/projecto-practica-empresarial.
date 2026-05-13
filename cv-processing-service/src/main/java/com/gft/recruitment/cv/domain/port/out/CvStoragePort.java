package com.gft.recruitment.cv.domain.port.out;

import com.gft.recruitment.cv.domain.model.CvMetadata;
import com.gft.recruitment.cv.domain.model.CvStatus;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CvStoragePort {
    Mono<CvMetadata> save(CvMetadata metadata);
    Mono<CvMetadata> findById(UUID id);
    Mono<CvMetadata> updateStatus(UUID id, CvStatus status, String errorMessage);
    Mono<CvMetadata> updateExtractedText(UUID id, String extractedText);
    Mono<CvMetadata> findByCandidateAndJobOffer(UUID candidateId, UUID jobOfferId);
}
