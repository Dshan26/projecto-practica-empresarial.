package com.gft.recruitment.cv.application.service;

import com.gft.recruitment.cv.application.dto.CvMetadataResponse;
import com.gft.recruitment.cv.application.dto.UploadCvCommand;
import com.gft.recruitment.cv.domain.exception.CvNotFoundException;
import com.gft.recruitment.cv.domain.exception.InvalidFileException;
import com.gft.recruitment.cv.domain.model.CvMetadata;
import com.gft.recruitment.cv.domain.model.CvStatus;
import com.gft.recruitment.cv.domain.port.in.ExtractTextUseCase;
import com.gft.recruitment.cv.domain.port.in.UploadCvUseCase;
import com.gft.recruitment.cv.domain.port.out.CvEventPublisherPort;
import com.gft.recruitment.cv.domain.port.out.CvStoragePort;
import com.gft.recruitment.events.CvUploadedEvent;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CvProcessingService implements UploadCvUseCase, ExtractTextUseCase {

    private static final Logger log = LoggerFactory.getLogger(CvProcessingService.class);
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB
    private static final byte[] PDF_MAGIC_BYTES = {0x25, 0x50, 0x44, 0x46}; // %PDF
    private static final String UPLOADS_DIR = "uploads";

    private final CvStoragePort cvStoragePort;
    private final CvEventPublisherPort cvEventPublisherPort;

    public CvProcessingService(CvStoragePort cvStoragePort, CvEventPublisherPort cvEventPublisherPort) {
        this.cvStoragePort = cvStoragePort;
        this.cvEventPublisherPort = cvEventPublisherPort;
    }

    @Override
    public Mono<CvMetadataResponse> upload(UploadCvCommand command) {
        return Mono.defer(() -> {
            validatePdf(command.fileContent(), command.fileSize());

            UUID cvId = UUID.randomUUID();
            String filePath = saveFileToLocalStorage(cvId, command.fileName(), command.fileContent());

            String extractedText;
            CvStatus status;
            String errorMessage = null;

            try {
                extractedText = extractTextFromPdf(command.fileContent());
                status = CvStatus.PROCESSED;
            } catch (Exception e) {
                log.error("Error extracting text from PDF: {}", e.getMessage());
                extractedText = null;
                status = CvStatus.ERROR;
                errorMessage = "Failed to extract text: " + e.getMessage();
            }

            CvMetadata metadata = new CvMetadata();
            metadata.setId(cvId);
            metadata.setCandidateId(command.candidateId());
            metadata.setJobOfferId(command.jobOfferId());
            metadata.setFileName(command.fileName());
            metadata.setFileSize(command.fileSize());
            metadata.setFilePath(filePath);
            metadata.setExtractedText(extractedText);
            metadata.setStatus(status);
            metadata.setErrorMessage(errorMessage);
            metadata.setUploadedAt(LocalDateTime.now());

            return cvStoragePort.save(metadata)
                    .flatMap(saved -> {
                        if (saved.getStatus() == CvStatus.PROCESSED) {
                            CvUploadedEvent event = new CvUploadedEvent(
                                    UUID.randomUUID(),
                                    Instant.now(),
                                    saved.getId(),
                                    saved.getCandidateId(),
                                    saved.getJobOfferId(),
                                    saved.getFileName(),
                                    saved.getFileSize()
                            );
                            return cvEventPublisherPort.publishCvUploaded(event)
                                    .thenReturn(saved);
                        }
                        return Mono.just(saved);
                    })
                    .map(this::toResponse);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<String> extractText(UUID cvId) {
        return cvStoragePort.findById(cvId)
                .switchIfEmpty(Mono.error(new CvNotFoundException(cvId)))
                .map(metadata -> {
                    if (metadata.getExtractedText() == null) {
                        throw new CvNotFoundException(cvId);
                    }
                    return metadata.getExtractedText();
                });
    }

    private void validatePdf(byte[] fileContent, long fileSize) {
        if (fileContent == null || fileContent.length == 0) {
            throw new InvalidFileException("File content is empty");
        }
        if (fileSize > MAX_FILE_SIZE) {
            throw new InvalidFileException("File size exceeds maximum allowed size of 10 MB");
        }
        if (fileContent.length < PDF_MAGIC_BYTES.length) {
            throw new InvalidFileException("Invalid file format: not a PDF file");
        }
        for (int i = 0; i < PDF_MAGIC_BYTES.length; i++) {
            if (fileContent[i] != PDF_MAGIC_BYTES[i]) {
                throw new InvalidFileException("Invalid file format: not a PDF file");
            }
        }
    }

    private String saveFileToLocalStorage(UUID cvId, String fileName, byte[] fileContent) {
        try {
            Path uploadsPath = Paths.get(UPLOADS_DIR);
            if (!Files.exists(uploadsPath)) {
                Files.createDirectories(uploadsPath);
            }
            String storedFileName = cvId + "_" + fileName;
            Path filePath = uploadsPath.resolve(storedFileName);
            Files.write(filePath, fileContent);
            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
        }
    }

    private String extractTextFromPdf(byte[] fileContent) {
        try (PDDocument document = Loader.loadPDF(fileContent)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String rawText = stripper.getText(document);
            return cleanText(rawText);
        } catch (IOException e) {
            throw new RuntimeException("Failed to extract text from PDF: " + e.getMessage(), e);
        }
    }

    private String cleanText(String text) {
        if (text == null || text.isBlank()) {
            return "";
        }
        // Remove special characters except basic punctuation
        String cleaned = text.replaceAll("[^\\p{L}\\p{N}\\p{P}\\s]", "");
        // Replace multiple spaces with single space
        cleaned = cleaned.replaceAll("\\s{2,}", " ");
        // Replace multiple newlines with single newline
        cleaned = cleaned.replaceAll("(\\r?\\n){2,}", "\n");
        return cleaned.trim();
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
}
