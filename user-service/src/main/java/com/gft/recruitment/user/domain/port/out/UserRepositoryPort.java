package com.gft.recruitment.user.domain.port.out;

import com.gft.recruitment.user.domain.model.User;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepositoryPort {
    Mono<User> save(User user);
    Mono<User> findByEmail(String email);
    Mono<User> findById(UUID id);
}
