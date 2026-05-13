package com.gft.recruitment.user.domain.port.in;

import com.gft.recruitment.user.application.dto.AuthCommand;
import com.gft.recruitment.user.application.dto.AuthResponse;
import reactor.core.publisher.Mono;

public interface AuthenticateUserUseCase {
    Mono<AuthResponse> authenticate(AuthCommand command);
}
