package com.gft.recruitment.joboffer.application.dto;

import com.gft.recruitment.joboffer.domain.model.SeniorityLevel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record JobOfferResponse(
    UUID id,
    String nombreCargo,
    String descripcion,
    List<String> habilidadesRequeridas,
    int experienciaMinimaAnios,
    SeniorityLevel seniorityEsperado,
    UUID recruiterId,
    LocalDateTime fechaCreacion
) {}
