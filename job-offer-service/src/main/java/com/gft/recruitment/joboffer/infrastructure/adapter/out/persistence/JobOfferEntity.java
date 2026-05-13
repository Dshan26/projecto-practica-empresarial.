package com.gft.recruitment.joboffer.infrastructure.adapter.out.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("job_offers")
public class JobOfferEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column("nombre_cargo")
    private String nombreCargo;

    private String descripcion;

    @Column("experiencia_minima_anios")
    private int experienciaMinimaAnios;

    @Column("seniority_esperado")
    private String seniorityEsperado;

    @Column("recruiter_id")
    private UUID recruiterId;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Transient
    private boolean isNew = true;

    public JobOfferEntity() {
    }

    @Override
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    @Override
    @Transient
    public boolean isNew() { return isNew; }
    public void setNew(boolean isNew) { this.isNew = isNew; }

    public String getNombreCargo() { return nombreCargo; }
    public void setNombreCargo(String nombreCargo) { this.nombreCargo = nombreCargo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getExperienciaMinimaAnios() { return experienciaMinimaAnios; }
    public void setExperienciaMinimaAnios(int experienciaMinimaAnios) { this.experienciaMinimaAnios = experienciaMinimaAnios; }

    public String getSeniorityEsperado() { return seniorityEsperado; }
    public void setSeniorityEsperado(String seniorityEsperado) { this.seniorityEsperado = seniorityEsperado; }

    public UUID getRecruiterId() { return recruiterId; }
    public void setRecruiterId(UUID recruiterId) { this.recruiterId = recruiterId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
