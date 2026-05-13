package com.gft.recruitment.cv.infrastructure.adapter.in.web;

import com.gft.recruitment.cv.application.dto.CvMetadataResponse;
import com.gft.recruitment.cv.application.dto.UploadCvCommand;
import com.gft.recruitment.cv.domain.exception.CvNotFoundException;
import com.gft.recruitment.cv.domain.model.CvMetadata;
import com.gft.recruitment.cv.domain.port.in.ExtractTextUseCase;
import com.gft.recruitment.cv.domain.port.in.UploadCvUseCase;
import com.gft.recruitment.cv.domain.port.out.CvStoragePort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/cv")
public class CvController {

    private final UploadCvUseCase uploadCvUseCase;
    private final ExtractTextUseCase extractTextUseCase;
    private final CvStoragePort cvStoragePort;

    public CvController(UploadCvUseCase uploadCvUseCase,
                        ExtractTextUseCase extractTextUseCase,
                        CvStoragePort cvStoragePort) {
        this.uploadCvUseCase = uploadCvUseCase;
        this.extractTextUseCase = extractTextUseCase;
        this.cvStoragePort = cvStoragePort;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CvMetadataResponse> upload(
            @RequestPart("file") FilePart filePart,
            @RequestPart("jobOfferId") String jobOfferId,
            @RequestHeader("X-User-Id") UUID candidateId) {

        return filePart.content()
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    return bytes;
                })
                .reduce(this::concatArrays)
                .flatMap(fileContent -> {
                    UploadCvCommand command = new UploadCvCommand(
                            candidateId,
                            UUID.fromString(jobOfferId),
                            filePart.filename(),
                            fileContent.length,
                            fileContent
                    );
                    return uploadCvUseCase.upload(command);
                });
    }

    @GetMapping("/{id}")
    public Mono<CvMetadataResponse> findById(@PathVariable UUID id) {
        return cvStoragePort.findById(id)
                .switchIfEmpty(Mono.error(new CvNotFoundException(id)))
                .map(this::toResponse);
    }

    @GetMapping("/{id}/text")
    public Mono<String> getExtractedText(@PathVariable UUID id) {
        return extractTextUseCase.extractText(id);
    }

    @GetMapping("/check/{jobOfferId}")
    public Mono<CvMetadataResponse> checkCvUploaded(
            @PathVariable UUID jobOfferId,
            @RequestHeader(value = "X-User-Id", required = false) UUID candidateId) {
        if (candidateId == null) return Mono.empty();
        return cvStoragePort.findByCandidateAndJobOffer(candidateId, jobOfferId)
                .map(this::toResponse);
    }

    private CvMetadataResponse toResponse(CvMetadata metadata) {
        return new CvMetadataResponse(
                metadata.getId(),
                metadata.getCandidateId(),
                metadata.getJobOfferId(),
                metadata.getFileName(),
                metadata.getFileSize(),
                metadata.getStatus(),
                metadata.getUploadedAt()
        );
    }

    private byte[] concatArrays(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}
