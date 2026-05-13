package com.gft.recruitment.user.application.dto;

import com.gft.recruitment.user.domain.model.UserRole;

public record RegisterUserCommand(
    String nombre,
    String email,
    String password,
    UserRole rol
) {}
