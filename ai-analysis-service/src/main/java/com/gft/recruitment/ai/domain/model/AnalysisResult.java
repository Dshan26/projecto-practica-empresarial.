package com.gft.recruitment.ai.domain.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AnalysisResult {

    private String id;
    private UUID cvId;
    private UUID candidateId;
    private UUID jobOfferId;
    private List<IdentifiedSkill> habilidadesIdentificadas;
    private int aniosExperiencia;
    private SeniorityLevel seniorityDetectado;
    private String resumenProfesional;
    private String rawOpenAiResponse;
    private AnalysisStatus status;
    private String errorMessage;
    private LocalDateTime analyzedAt;
    private int retryCount;

    public AnalysisResult() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public UUID getCvId() { return cvId; }
    public void setCvId(UUID cvId) { this.cvId = cvId; }

    public UUID getCandidateId() { return candidateId; }
    public void setCandidateId(UUID candidateId) { this.candidateId = candidateId; }

    public UUID getJobOfferId() { return jobOfferId; }
    public void setJobOfferId(UUID jobOfferId) { this.jobOfferId = jobOfferId; }

    public List<IdentifiedSkill> getHabilidadesIdentificadas() { return habilidadesIdentificadas; }
    public void setHabilidadesIdentificadas(List<IdentifiedSkill> habilidadesIdentificadas) { this.habilidadesIdentificadas = habilidadesIdentificadas; }

    public int getAniosExperiencia() { return aniosExperiencia; }
    public void setAniosExperiencia(int aniosExperiencia) { this.aniosExperiencia = aniosExperiencia; }

    public SeniorityLevel getSeniorityDetectado() { return seniorityDetectado; }
    public void setSeniorityDetectado(SeniorityLevel seniorityDetectado) { this.seniorityDetectado = seniorityDetectado; }

    public String getResumenProfesional() { return resumenProfesional; }
    public void setResumenProfesional(String resumenProfesional) { this.resumenProfesional = resumenProfesional; }

    public String getRawOpenAiResponse() { return rawOpenAiResponse; }
    public void setRawOpenAiResponse(String rawOpenAiResponse) { this.rawOpenAiResponse = rawOpenAiResponse; }

    public AnalysisStatus getStatus() { return status; }
    public void setStatus(AnalysisStatus status) { this.status = status; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public LocalDateTime getAnalyzedAt() { return analyzedAt; }
    public void setAnalyzedAt(LocalDateTime analyzedAt) { this.analyzedAt = analyzedAt; }

    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
}
