package com.gft.recruitment.user.infrastructure.adapter.in.web;

import com.gft.recruitment.user.application.dto.AuthCommand;
import com.gft.recruitment.user.application.dto.AuthResponse;
import com.gft.recruitment.user.application.dto.RegisterUserCommand;
import com.gft.recruitment.user.application.dto.UserResponse;
import com.gft.recruitment.user.domain.port.in.AuthenticateUserUseCase;
import com.gft.recruitment.user.domain.port.in.RegisterUserUseCase;
import com.gft.recruitment.user.domain.port.out.UserRepositoryPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final UserRepositoryPort userRepositoryPort;

    public AuthController(RegisterUserUseCase registerUserUseCase,
                          AuthenticateUserUseCase authenticateUserUseCase,
                          UserRepositoryPort userRepositoryPort) {
        this.registerUserUseCase = registerUserUseCase;
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.userRepositoryPort = userRepositoryPort;
    }

    @PostMapping("/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserResponse> register(@RequestBody RegisterUserCommand command) {
        return registerUserUseCase.register(command);
    }

    @PostMapping("/auth/login")
    public Mono<AuthResponse> login(@RequestBody AuthCommand command) {
        return authenticateUserUseCase.authenticate(command);
    }

    @GetMapping("/users/{id}")
    public Mono<UserResponse> getUserById(@PathVariable UUID id) {
        return userRepositoryPort.findById(id)
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getNombre(),
                        user.getEmail(),
                        user.getRol()
                ));
    }
}
