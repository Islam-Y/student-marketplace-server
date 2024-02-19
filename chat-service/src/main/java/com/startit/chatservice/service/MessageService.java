package com.startit.chatservice.service;

import com.startit.chatservice.entity.MessageEntity;
import com.startit.chatservice.mapper.MessageMapper;
import com.startit.chatservice.repository.ChatRepo;
import com.startit.chatservice.repository.MessageRepo;
import com.startit.chatservice.transfer.Message;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class MessageService {

    private final MessageRepo repo;
    private final ChatRepo chatRepo;
    private final UserServiceClient userServiceClient;

    private static final MessageMapper MAPPER = MessageMapper.INSTANCE;

    public Mono<Message> save(Long chatId, Message message) {
        return Mono.zip(
                        chatRepo.findById(chatId),
                        userServiceClient.getUser(message.getSenderId())
                )
                .map(tuple -> {
                    var entity = MAPPER.toEntity(message);
                    entity.setChatId(tuple.getT1().getId());
                    entity.setSenderId(tuple.getT2().getId());
                    return entity;
                })
                .flatMap(repo::save)
                .map(MAPPER::toDto);
    }

    public Flux<Message> getMessages(Long chatId, Pageable pageable) {
        return repo.findByChatId(chatId, pageable)
                .map(MAPPER::toDto);
    }
}
