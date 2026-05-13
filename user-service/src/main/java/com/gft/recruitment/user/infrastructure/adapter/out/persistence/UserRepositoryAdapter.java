package com.gft.recruitment.user.infrastructure.adapter.out.persistence;

import com.gft.recruitment.user.domain.model.User;
import com.gft.recruitment.user.domain.model.UserRole;
import com.gft.recruitment.user.domain.port.out.UserRepositoryPort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserR2dbcRepository repository;

    public UserRepositoryAdapter(UserR2dbcRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<User> save(User user) {
        UserEntity entity = toEntity(user);
        return repository.save(entity).map(this::toDomain);
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public Mono<User> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    private UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setNombre(user.getNombre());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPasswordHash());
        entity.setRol(user.getRol().name());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());
        entity.setNew(true);
        return entity;
    }

    private User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getNombre(),
                entity.getEmail(),
                entity.getPassword(),
                UserRole.valueOf(entity.getRol()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
