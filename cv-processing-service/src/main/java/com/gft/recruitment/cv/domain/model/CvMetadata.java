package com.gft.recruitment.cv.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class CvMetadata {

    private UUID id;
    private UUID candidateId;
    private UUID jobOfferId;
    private String fileName;
    private long fileSize;
    private String filePath;
    private String extractedText;
    private CvStatus status;
    private String errorMessage;
    private LocalDateTime uploadedAt;

    public CvMetadata() {
    }

    public CvMetadata(UUID id, UUID candidateId, UUID jobOfferId, String fileName, long fileSize,
                      String filePath, String extractedText, CvStatus status, String errorMessage,
                      LocalDateTime uploadedAt) {
        this.id = id;
        this.candidateId = candidateId;
        this.jobOfferId = jobOfferId;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.filePath = filePath;
        this.extractedText = extractedText;
        this.status = status;
        this.errorMessage = errorMessage;
        this.uploadedAt = uploadedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getCandidateId() { return candidateId; }
    public void setCandidateId(UUID candidateId) { this.candidateId = candidateId; }

    public UUID getJobOfferId() { return jobOfferId; }
    public void setJobOfferId(UUID jobOfferId) { this.jobOfferId = jobOfferId; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getExtractedText() { return extractedText; }
    public void setExtractedText(String extractedText) { this.extractedText = extractedText; }

    public CvStatus getStatus() { return status; }
    public void setStatus(CvStatus status) { this.status = status; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}
