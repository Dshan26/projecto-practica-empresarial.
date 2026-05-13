package com.gft.recruitment.user.application.dto;

public record AuthCommand(
    String email,
    String password
) {}
