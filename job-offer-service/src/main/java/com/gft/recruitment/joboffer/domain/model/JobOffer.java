package com.gft.recruitment.joboffer.domain.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class JobOffer {

    private UUID id;
    private String nombreCargo;
    private String descripcion;
    private List<String> habilidadesRequeridas;
    private int experienciaMinimaAnios;
    private SeniorityLevel seniorityEsperado;
    private UUID recruiterId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public JobOffer() {
    }

    public JobOffer(UUID id, String nombreCargo, String descripcion, List<String> habilidadesRequeridas,
                    int experienciaMinimaAnios, SeniorityLevel seniorityEsperado, UUID recruiterId,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nombreCargo = nombreCargo;
        this.descripcion = descripcion;
        this.habilidadesRequeridas = habilidadesRequeridas;
        this.experienciaMinimaAnios = experienciaMinimaAnios;
        this.seniorityEsperado = seniorityEsperado;
        this.recruiterId = recruiterId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNombreCargo() { return nombreCargo; }
    public void setNombreCargo(String nombreCargo) { this.nombreCargo = nombreCargo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<String> getHabilidadesRequeridas() { return habilidadesRequeridas; }
    public void setHabilidadesRequeridas(List<String> habilidadesRequeridas) { this.habilidadesRequeridas = habilidadesRequeridas; }

    public int getExperienciaMinimaAnios() { return experienciaMinimaAnios; }
    public void setExperienciaMinimaAnios(int experienciaMinimaAnios) { this.experienciaMinimaAnios = experienciaMinimaAnios; }

    public SeniorityLevel getSeniorityEsperado() { return seniorityEsperado; }
    public void setSeniorityEsperado(SeniorityLevel seniorityEsperado) { this.seniorityEsperado = seniorityEsperado; }

    public UUID getRecruiterId() { return recruiterId; }
    public void setRecruiterId(UUID recruiterId) { this.recruiterId = recruiterId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
