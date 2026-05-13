package com.gft.recruitment.joboffer.application.dto;

import com.gft.recruitment.joboffer.domain.model.SeniorityLevel;

import java.util.List;

public record UpdateJobOfferCommand(
    String nombreCargo,
    String descripcion,
    List<String> habilidadesRequeridas,
    int experienciaMinimaAnios,
    SeniorityLevel seniorityEsperado
) {}
