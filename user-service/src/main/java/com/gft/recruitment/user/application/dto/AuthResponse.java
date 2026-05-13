package com.gft.recruitment.user.application.dto;

import com.gft.recruitment.user.domain.model.UserRole;

public record AuthResponse(
    String token,
    String email,
    UserRole rol
) {}
