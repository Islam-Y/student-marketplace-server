package com.startit.chatservice.repository;

import com.startit.chatservice.entity.MessageEntity;
import com.startit.chatservice.entity.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface UserRepo extends ReactiveCrudRepository<UserEntity, Long> {

    Flux<MessageEntity> findByUsername(String username);
}
