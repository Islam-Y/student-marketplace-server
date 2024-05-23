package com.startit.chatservice.repository;

import com.startit.chatservice.entity.UserMessageSequence;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface UserMessageSequenceRepo extends ReactiveCrudRepository<UserMessageSequence, Long> {

    @Query("SELECT * FROM message_sequence WHERE user_id = :userId ORDER BY seq_number DESC LIMIT 1")
    Mono<UserMessageSequence> findTopByUserIdOrderBySeqNumberDesc(Long userId);

    @Query("SELECT * FROM message_sequence WHERE user_id = :userId AND seq_number > :lastMessageId ORDER BY seq_number")
    Flux<UserMessageSequence> findByUserIdAndMessageIdGreaterThanOrderByMessageId(Long userId, Long lastMessageId);

}