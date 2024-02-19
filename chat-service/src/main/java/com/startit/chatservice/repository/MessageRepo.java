package com.startit.chatservice.repository;

import com.startit.chatservice.entity.MessageEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface MessageRepo extends ReactiveCrudRepository<MessageEntity, Long> {
    public Flux<MessageEntity> findByChatId(Long chatId, Pageable pageable);
}
