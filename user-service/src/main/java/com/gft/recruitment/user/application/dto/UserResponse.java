package com.gft.recruitment.user.application.dto;

import com.gft.recruitment.user.domain.model.UserRole;

import java.util.UUID;

public record UserResponse(
    UUID id,
    String nombre,
    String email,
    UserRole rol
) {}
