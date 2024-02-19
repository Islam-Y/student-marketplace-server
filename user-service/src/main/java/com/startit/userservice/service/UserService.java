package com.startit.userservice.service;

import com.startit.userservice.mapper.UserMapper;
import com.startit.userservice.repository.UserRepo;
import com.startit.userservice.transfer.User;
import com.startit.userservice.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepo repo;

    public Mono<Long> save(User user) {
        return Mono.fromCallable(() -> UserMapper.INSTANCE.toEntity(user))
                .map(repo::save)
                .map(UserEntity::getId);
    }

    public Mono<Optional<User>> findById(Long id) {
        return Mono.fromCallable(() -> repo.findById(id))
                .map(userEntity -> userEntity.map(UserMapper.INSTANCE::toDto));
    }

    public Mono<Optional<User>> findByUsername(String username) {
        return Mono.fromCallable(() -> repo.findByUsername(username))
                .map(userEntity -> userEntity.map(UserMapper.INSTANCE::toDto));
    }
}
