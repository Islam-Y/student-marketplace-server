package com.startit.chatservice.service;

import com.startit.chatservice.entity.ChatEntity;
import com.startit.chatservice.mapper.ChatMapper;
import com.startit.chatservice.repository.ChatRepo;
import com.startit.chatservice.transfer.Chat;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ChatService {

    private final ChatRepo repo;
    private final ItemService itemService;

    private static final ChatMapper MAPPER = ChatMapper.INSTANCE;

    public Mono<Chat> save(Chat chat) {
        return repo.save(MAPPER.toEntity(chat))
                .map(MAPPER::toDto);
    }

    public Flux<Chat> getUserChats(Long userId, Pageable pageable) {
        return Flux.fromStream(itemService.getItemByUser(userId, pageable).stream())
                .flatMap(item -> repo.findByItemId(item.getId(), pageable)
                        .onErrorResume(Exception.class, e -> Flux.empty()))
                .mergeWith(repo.findByCustomerId(userId, pageable)
                        .onErrorResume(Exception.class, e -> Flux.empty()))
                .distinct()
                .map(MAPPER::toDto);
    }
}

