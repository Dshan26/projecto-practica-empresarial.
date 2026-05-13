package com.gft.recruitment.user.domain.port.in;

import com.gft.recruitment.user.application.dto.RegisterUserCommand;
import com.gft.recruitment.user.application.dto.UserResponse;
import reactor.core.publisher.Mono;

public interface RegisterUserUseCase {
    Mono<UserResponse> register(RegisterUserCommand command);
}
