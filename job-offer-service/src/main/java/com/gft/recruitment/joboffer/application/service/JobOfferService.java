package com.gft.recruitment.joboffer.application.service;

import com.gft.recruitment.joboffer.application.dto.CreateJobOfferCommand;
import com.gft.recruitment.joboffer.application.dto.JobOfferResponse;
import com.gft.recruitment.joboffer.application.dto.UpdateJobOfferCommand;
import com.gft.recruitment.joboffer.domain.exception.InvalidJobOfferException;
import com.gft.recruitment.joboffer.domain.exception.JobOfferNotFoundException;
import com.gft.recruitment.joboffer.domain.model.JobOffer;
import com.gft.recruitment.joboffer.domain.port.in.CreateJobOfferUseCase;
import com.gft.recruitment.joboffer.domain.port.in.DeleteJobOfferUseCase;
import com.gft.recruitment.joboffer.domain.port.in.QueryJobOfferUseCase;
import com.gft.recruitment.joboffer.domain.port.in.UpdateJobOfferUseCase;
import com.gft.recruitment.joboffer.domain.port.out.JobOfferRepositoryPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class JobOfferService implements CreateJobOfferUseCase, QueryJobOfferUseCase,
        UpdateJobOfferUseCase, DeleteJobOfferUseCase {

    private final JobOfferRepositoryPort jobOfferRepository;

    public JobOfferService(JobOfferRepositoryPort jobOfferRepository) {
        this.jobOfferRepository = jobOfferRepository;
    }

    @Override
    public Mono<JobOfferResponse> create(CreateJobOfferCommand command, UUID recruiterId) {
        return Mono.defer(() -> {
            validate(command.nombreCargo(), command.habilidadesRequeridas());

            JobOffer jobOffer = new JobOffer();
            jobOffer.setId(UUID.randomUUID());
            jobOffer.setNombreCargo(command.nombreCargo());
            jobOffer.setDescripcion(command.descripcion());
            jobOffer.setHabilidadesRequeridas(command.habilidadesRequeridas());
            jobOffer.setExperienciaMinimaAnios(command.experienciaMinimaAnios());
            jobOffer.setSeniorityEsperado(command.seniorityEsperado());
            jobOffer.setRecruiterId(recruiterId);
            jobOffer.setCreatedAt(LocalDateTime.now());
            jobOffer.setUpdatedAt(LocalDateTime.now());

            return jobOfferRepository.save(jobOffer)
                    .flatMap(saved -> jobOfferRepository.saveSkills(saved.getId(), command.habilidadesRequeridas())
                            .thenReturn(saved))
                    .flatMap(this::toResponse);
        });
    }

    @Override
    public Mono<JobOfferResponse> findById(UUID id) {
        return jobOfferRepository.findById(id)
                .switchIfEmpty(Mono.error(new JobOfferNotFoundException(id)))
                .flatMap(this::toResponse);
    }

    @Override
    public Flux<JobOfferResponse> findAll() {
        return jobOfferRepository.findAll()
                .flatMap(this::toResponse);
    }

    @Override
    public Mono<JobOfferResponse> update(UUID id, UpdateJobOfferCommand command) {
        return jobOfferRepository.findById(id)
                .switchIfEmpty(Mono.error(new JobOfferNotFoundException(id)))
                .flatMap(existing -> {
                    validate(command.nombreCargo(), command.habilidadesRequeridas());

                    existing.setNombreCargo(command.nombreCargo());
                    existing.setDescripcion(command.descripcion());
                    existing.setHabilidadesRequeridas(command.habilidadesRequeridas());
                    existing.setExperienciaMinimaAnios(command.experienciaMinimaAnios());
                    existing.setSeniorityEsperado(command.seniorityEsperado());
                    existing.setUpdatedAt(LocalDateTime.now());

                    return jobOfferRepository.save(existing)
                            .flatMap(saved -> jobOfferRepository.deleteSkillsByJobOfferId(saved.getId())
                                    .then(jobOfferRepository.saveSkills(saved.getId(), command.habilidadesRequeridas()))
                                    .thenReturn(saved))
                            .flatMap(this::toResponse);
                });
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return jobOfferRepository.findById(id)
                .switchIfEmpty(Mono.error(new JobOfferNotFoundException(id)))
                .flatMap(existing -> jobOfferRepository.deleteById(id));
    }

    private void validate(String nombreCargo, List<String> habilidades) {
        if (nombreCargo == null || nombreCargo.isBlank()) {
            throw new InvalidJobOfferException("El nombre del cargo es obligatorio");
        }
        if (habilidades == null || habilidades.isEmpty()) {
            throw new InvalidJobOfferException("Se requiere al menos una habilidad");
        }
    }

    private Mono<JobOfferResponse> toResponse(JobOffer jobOffer) {
        return jobOfferRepository.findSkillsByJobOfferId(jobOffer.getId())
                .collectList()
                .map(skills -> new JobOfferResponse(
                        jobOffer.getId(),
                        jobOffer.getNombreCargo(),
                        jobOffer.getDescripcion(),
                        skills,
                        jobOffer.getExperienciaMinimaAnios(),
                        jobOffer.getSeniorityEsperado(),
                        jobOffer.getRecruiterId(),
                        jobOffer.getCreatedAt()
                ));
    }
}
