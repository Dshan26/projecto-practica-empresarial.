package com.gft.recruitment.cv.infrastructure.adapter.out.persistence;

import com.gft.recruitment.cv.domain.model.CvMetadata;
import com.gft.recruitment.cv.domain.model.CvStatus;
import com.gft.recruitment.cv.domain.port.out.CvStoragePort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CvStorageAdapter implements CvStoragePort {

    private final CvMetadataR2dbcRepository repository;

    public CvStorageAdapter(CvMetadataR2dbcRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<CvMetadata> save(CvMetadata metadata) {
        CvMetadataEntity entity = toEntity(metadata);
        return repository.save(entity).map(this::toDomain);
    }

    @Override
    public Mono<CvMetadata> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Mono<CvMetadata> updateStatus(UUID id, CvStatus status, String errorMessage) {
        return repository.findById(id)
                .flatMap(entity -> {
                    entity.setStatus(status.name());
                    entity.setErrorMessage(errorMessage);
                    return repository.save(entity);
                })
                .map(this::toDomain);
    }

    @Override
    public Mono<CvMetadata> updateExtractedText(UUID id, String extractedText) {
        return repository.findById(id)
                .flatMap(entity -> {
                    entity.setExtractedText(extractedText);
                    entity.setStatus(CvStatus.PROCESSED.name());
                    entity.setNew(false);
                    return repository.save(entity);
                })
                .map(this::toDomain);
    }

    @Override
    public Mono<CvMetadata> findByCandidateAndJobOffer(UUID candidateId, UUID jobOfferId) {
        return repository.findByCandidateIdAndJobOfferId(candidateId, jobOfferId)
                .map(this::toDomain);
    }

    private CvMetadataEntity toEntity(CvMetadata metadata) {
        CvMetadataEntity entity = new CvMetadataEntity();
        entity.setId(metadata.getId());
        entity.setCandidateId(metadata.getCandidateId());
        entity.setJobOfferId(metadata.getJobOfferId());
        entity.setFileName(metadata.getFileName());
        entity.setFileSize(metadata.getFileSize());
        entity.setFilePath(metadata.getFilePath());
        entity.setExtractedText(metadata.getExtractedText());
        entity.setStatus(metadata.getStatus().name());
        entity.setErrorMessage(metadata.getErrorMessage());
        entity.setUploadedAt(metadata.getUploadedAt());
        entity.setNew(true);
        return entity;
    }

    private CvMetadata toDomain(CvMetadataEntity entity) {
        return new CvMetadata(
                entity.getId(),
                entity.getCandidateId(),
                entity.getJobOfferId(),
                entity.getFileName(),
                entity.getFileSize(),
                entity.getFilePath(),
                entity.getExtractedText(),
                CvStatus.valueOf(entity.getStatus()),
                entity.getErrorMessage(),
                entity.getUploadedAt()
        );
    }
}
