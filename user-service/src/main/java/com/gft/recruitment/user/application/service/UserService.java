package com.gft.recruitment.user.application.service;

import com.gft.recruitment.user.application.dto.AuthCommand;
import com.gft.recruitment.user.application.dto.AuthResponse;
import com.gft.recruitment.user.application.dto.RegisterUserCommand;
import com.gft.recruitment.user.application.dto.UserResponse;
import com.gft.recruitment.user.domain.exception.InvalidCredentialsException;
import com.gft.recruitment.user.domain.exception.UserAlreadyExistsException;
import com.gft.recruitment.user.domain.model.User;
import com.gft.recruitment.user.domain.port.in.AuthenticateUserUseCase;
import com.gft.recruitment.user.domain.port.in.RegisterUserUseCase;
import com.gft.recruitment.user.domain.port.out.UserRepositoryPort;
import com.gft.recruitment.user.infrastructure.config.JwtConfig;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService implements RegisterUserUseCase, AuthenticateUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;

    public UserService(UserRepositoryPort userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtConfig jwtConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
    }

    @Override
    public Mono<UserResponse> register(RegisterUserCommand command) {
        return userRepository.findByEmail(command.email())
                .flatMap(existing -> Mono.<UserResponse>error(
                        new UserAlreadyExistsException(command.email())))
                .switchIfEmpty(Mono.defer(() -> {
                    User user = new User();
                    user.setId(UUID.randomUUID());
                    user.setNombre(command.nombre());
                    user.setEmail(command.email());
                    user.setPasswordHash(passwordEncoder.encode(command.password()));
                    user.setRol(command.rol());
                    user.setCreatedAt(LocalDateTime.now());
                    user.setUpdatedAt(LocalDateTime.now());

                    return userRepository.save(user)
                            .map(saved -> new UserResponse(
                                    saved.getId(),
                                    saved.getNombre(),
                                    saved.getEmail(),
                                    saved.getRol()
                            ));
                }));
    }

    @Override
    public Mono<AuthResponse> authenticate(AuthCommand command) {
        return userRepository.findByEmail(command.email())
                .filter(user -> passwordEncoder.matches(command.password(), user.getPasswordHash()))
                .map(user -> new AuthResponse(
                        jwtConfig.generateToken(user.getId(), user.getEmail(), user.getRol()),
                        user.getEmail(),
                        user.getRol()
                ))
                .switchIfEmpty(Mono.error(new InvalidCredentialsException()));
    }
}
