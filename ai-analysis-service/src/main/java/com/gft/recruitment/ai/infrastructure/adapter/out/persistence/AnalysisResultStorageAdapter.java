package com.gft.recruitment.ai.infrastructure.adapter.out.persistence;

import com.gft.recruitment.ai.domain.model.AnalysisResult;
import com.gft.recruitment.ai.domain.port.out.AnalysisResultStoragePort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class AnalysisResultStorageAdapter implements AnalysisResultStoragePort {

    private final AnalysisResultMongoRepository repository;

    public AnalysisResultStorageAdapter(AnalysisResultMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<AnalysisResult> save(AnalysisResult result) {
        AnalysisResultDocument doc = toDocument(result);
        return repository.save(doc).map(this::toDomain);
    }

    @Override
    public Mono<AnalysisResult> findByCandidateAndJobOffer(UUID candidateId, UUID jobOfferId) {
        return repository.findByCandidateIdAndJobOfferId(candidateId, jobOfferId)
                .map(this::toDomain);
    }

    private AnalysisResultDocument toDocument(AnalysisResult result) {
        AnalysisResultDocument doc = new AnalysisResultDocument();
        doc.setId(result.getId());
        doc.setCvId(result.getCvId());
        doc.setCandidateId(result.getCandidateId());
        doc.setJobOfferId(result.getJobOfferId());
        doc.setHabilidadesIdentificadas(result.getHabilidadesIdentificadas());
        doc.setAniosExperiencia(result.getAniosExperiencia());
        doc.setSeniorityDetectado(result.getSeniorityDetectado());
        doc.setResumenProfesional(result.getResumenProfesional());
        doc.setRawOpenAiResponse(result.getRawOpenAiResponse());
        doc.setStatus(result.getStatus());
        doc.setErrorMessage(result.getErrorMessage());
        doc.setAnalyzedAt(result.getAnalyzedAt());
        doc.setRetryCount(result.getRetryCount());
        return doc;
    }

    private AnalysisResult toDomain(AnalysisResultDocument doc) {
        AnalysisResult result = new AnalysisResult();
        result.setId(doc.getId());
        result.setCvId(doc.getCvId());
        result.setCandidateId(doc.getCandidateId());
        result.setJobOfferId(doc.getJobOfferId());
        result.setHabilidadesIdentificadas(doc.getHabilidadesIdentificadas());
        result.setAniosExperiencia(doc.getAniosExperiencia());
        result.setSeniorityDetectado(doc.getSeniorityDetectado());
        result.setResumenProfesional(doc.getResumenProfesional());
        result.setRawOpenAiResponse(doc.getRawOpenAiResponse());
        result.setStatus(doc.getStatus());
        result.setErrorMessage(doc.getErrorMessage());
        result.setAnalyzedAt(doc.getAnalyzedAt());
        result.setRetryCount(doc.getRetryCount());
        return result;
    }
}
