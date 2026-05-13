package com.gft.recruitment.test.infrastructure.adapter.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gft.recruitment.test.domain.model.*;
import com.gft.recruitment.test.domain.port.out.TechnicalTestRepositoryPort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
public class TechnicalTestRepositoryAdapter implements TechnicalTestRepositoryPort {

    private final TechnicalTestR2dbcRepository testRepository;
    private final TestQuestionR2dbcRepository questionRepository;
    private final CandidateAnswerR2dbcRepository answerRepository;
    private final TestSubmissionR2dbcRepository submissionRepository;
    private final ObjectMapper objectMapper;

    public TechnicalTestRepositoryAdapter(TechnicalTestR2dbcRepository testRepository,
                                          TestQuestionR2dbcRepository questionRepository,
                                          CandidateAnswerR2dbcRepository answerRepository,
                                          TestSubmissionR2dbcRepository submissionRepository,
                                          ObjectMapper objectMapper) {
        this.testRepository = testRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.submissionRepository = submissionRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<TechnicalTest> saveTest(TechnicalTest test) {
        TechnicalTestEntity entity = toTestEntity(test);
        return testRepository.save(entity).map(this::toTestDomain);
    }

    @Override
    public Mono<TestQuestion> saveQuestion(TestQuestion question) {
        TestQuestionEntity entity = toQuestionEntity(question);
        return questionRepository.save(entity).map(this::toQuestionDomain);
    }

    @Override
    public Mono<TechnicalTest> findTestByJobOfferId(UUID jobOfferId) {
        return testRepository.findByJobOfferId(jobOfferId).map(this::toTestDomain);
    }

    @Override
    public Flux<TestQuestion> findQuestionsByTestId(UUID testId) {
        return questionRepository.findByTestIdOrderByOrdenAsc(testId).map(this::toQuestionDomain);
    }

    @Override
    public Mono<CandidateAnswer> saveAnswer(CandidateAnswer answer) {
        CandidateAnswerEntity entity = toAnswerEntity(answer);
        return answerRepository.save(entity).map(this::toAnswerDomain);
    }

    @Override
    public Mono<TestSubmission> saveSubmission(TestSubmission submission) {
        TestSubmissionEntity entity = toSubmissionEntity(submission);
        return submissionRepository.save(entity).map(this::toSubmissionDomain);
    }

    @Override
    public Mono<TestSubmission> findSubmission(UUID candidateId, UUID jobOfferId) {
        return submissionRepository.findByCandidateIdAndJobOfferId(candidateId, jobOfferId)
                .map(this::toSubmissionDomain);
    }

    @Override
    public Flux<CandidateAnswer> findAnswersByTestIdAndCandidateId(UUID testId, UUID candidateId) {
        return answerRepository.findByTestIdAndCandidateId(testId, candidateId)
                .map(this::toAnswerDomain);
    }

    // --- Mapping methods ---

    private TechnicalTestEntity toTestEntity(TechnicalTest test) {
        TechnicalTestEntity e = new TechnicalTestEntity();
        e.setId(test.getId());
        e.setJobOfferId(test.getJobOfferId());
        e.setCreatedAt(test.getCreatedAt());
        e.setNew(true);
        return e;
    }

    private TechnicalTest toTestDomain(TechnicalTestEntity e) {
        return new TechnicalTest(e.getId(), e.getJobOfferId(), e.getCreatedAt());
    }

    private TestQuestionEntity toQuestionEntity(TestQuestion q) {
        TestQuestionEntity e = new TestQuestionEntity();
        e.setId(q.getId());
        e.setTestId(q.getTestId());
        e.setEnunciado(q.getEnunciado());
        e.setTipo(q.getTipo().name());
        e.setRespuestaCorrecta(q.getRespuestaCorrecta());
        e.setOrden(q.getOrden());
        try {
            e.setOpciones(q.getOpciones() != null ? objectMapper.writeValueAsString(q.getOpciones()) : null);
        } catch (JsonProcessingException ex) {
            e.setOpciones(null);
        }
        e.setNew(true);
        return e;
    }

    private TestQuestion toQuestionDomain(TestQuestionEntity e) {
        List<String> opciones = null;
        try {
            if (e.getOpciones() != null) {
                opciones = objectMapper.readValue(e.getOpciones(), new TypeReference<>() {});
            }
        } catch (JsonProcessingException ex) {
            opciones = null;
        }
        return new TestQuestion(
                e.getId(), e.getTestId(), e.getEnunciado(),
                QuestionType.valueOf(e.getTipo()), opciones,
                e.getRespuestaCorrecta(), e.getOrden()
        );
    }

    private CandidateAnswerEntity toAnswerEntity(CandidateAnswer a) {
        CandidateAnswerEntity e = new CandidateAnswerEntity();
        e.setId(a.getId());
        e.setTestId(a.getTestId());
        e.setCandidateId(a.getCandidateId());
        e.setJobOfferId(a.getJobOfferId());
        e.setQuestionId(a.getQuestionId());
        e.setRespuesta(a.getRespuesta());
        e.setSubmittedAt(a.getSubmittedAt());
        e.setNew(true);
        return e;
    }

    private CandidateAnswer toAnswerDomain(CandidateAnswerEntity e) {
        return new CandidateAnswer(
                e.getId(), e.getTestId(), e.getCandidateId(), e.getJobOfferId(),
                e.getQuestionId(), e.getRespuesta(), e.getSubmittedAt()
        );
    }

    private TestSubmissionEntity toSubmissionEntity(TestSubmission s) {
        TestSubmissionEntity e = new TestSubmissionEntity();
        e.setId(s.getId());
        e.setTestId(s.getTestId());
        e.setCandidateId(s.getCandidateId());
        e.setJobOfferId(s.getJobOfferId());
        e.setSubmittedAt(s.getSubmittedAt());
        e.setNew(true);
        return e;
    }

    private TestSubmission toSubmissionDomain(TestSubmissionEntity e) {
        return new TestSubmission(
                e.getId(), e.getTestId(), e.getCandidateId(), e.getJobOfferId(), e.getSubmittedAt()
        );
    }
}
