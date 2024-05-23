package com.startit.chatservice.service;

import com.startit.chatservice.entity.ChatEntity;
import com.startit.chatservice.mapper.ChatMapper;
import com.startit.chatservice.repository.ChatRepo;
import com.startit.chatservice.transfer.Chat;
import com.startit.chatservice.transfer.Item;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public List<Chat> getUserChats(Long userId, Pageable pageable) {
        List<Item> items = itemService.getItemByUser(userId, pageable);

        Flux<ChatEntity> itemChatsFlux = Flux.fromIterable(items)
                .flatMap(item -> repo.findByItemId(item.getId(), pageable)
                        .onErrorResume(Exception.class, e -> Flux.empty()));

        Flux<ChatEntity> customerChatsFlux = repo.findByCustomerId(userId, pageable)
                .onErrorResume(Exception.class, e -> Flux.empty());

        List<ChatEntity> chats = Flux.merge(itemChatsFlux, customerChatsFlux)
                .distinct()
                .collectList()
                .block();

        if (chats == null) {
            return List.of();
        }

        return chats.stream()
                .map(MAPPER::toDto)
                .collect(Collectors.toList());
    }

}

