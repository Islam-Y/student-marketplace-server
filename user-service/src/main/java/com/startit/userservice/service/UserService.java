package com.startit.userservice.service;

import com.startit.shared.transfer.User;
import com.startit.userservice.mapper.UserMapper;
import com.startit.userservice.repository.UserRepo;
import com.startit.userservice.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserRepo repo;
    private final KafkaTemplate<String, User> kafkaTemplate;

    public void sendMessage(User user) {
        var future = kafkaTemplate.send("user-creation", user);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Sent message=[" + user +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                log.info("Unable to send message=[" +
                        user + "] due to : " + ex.getMessage());
            }
        });
    }

    public Mono<Long> save(User user) {
        return Mono.fromCallable(() -> UserMapper.INSTANCE.toEntity(user))
                .map(repo::save)
                .doOnSuccess(savedUser -> sendMessage(UserMapper.INSTANCE.toDto(savedUser)))
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
