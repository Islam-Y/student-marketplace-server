package com.startit.chatservice.repository;

import com.startit.chatservice.entity.ChatEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ChatRepo extends ReactiveCrudRepository<ChatEntity, Long> {
    Flux<ChatEntity> findByCustomerId(Long customerId, Pageable pageable);
    Flux<ChatEntity> findByItemId(Long sellerId, Pageable pageable);
}
