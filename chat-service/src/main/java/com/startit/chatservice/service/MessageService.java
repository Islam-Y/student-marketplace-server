package com.startit.chatservice.service;

import com.startit.chatservice.mapper.MessageMapper;
import com.startit.chatservice.mapper.UserMapper;
import com.startit.chatservice.repository.ChatRepo;
import com.startit.chatservice.repository.MessageRepo;
import com.startit.chatservice.repository.UserRepo;
import com.startit.chatservice.transfer.Message;
import com.startit.shared.transfer.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor
public class MessageService {

    private final MessageRepo repo;
    private final ChatRepo chatRepo;
    private final UserRepo userRepo;

    private static final MessageMapper MAPPER = MessageMapper.INSTANCE;
    private static final UserMapper USER_MAPPER = UserMapper.INSTANCE;

    public Mono<Message> save(Long chatId, Message message) {
        return Mono.zip(
                        chatRepo.findById(chatId),
                        userRepo.findById(message.getSenderId())
                )
                .map(tuple -> {
                    var entity = MAPPER.toEntity(message);
                    entity.setChatId(tuple.getT1().getId());
                    entity.setSenderId(tuple.getT2().orElseThrow().getId());
                    return entity;
                })
                .flatMap(repo::save)
                .map(MAPPER::toDto);
    }

    public Flux<Message> getMessages(Long chatId, Pageable pageable) {
        return repo.findByChatId(chatId, pageable)
                .map(MAPPER::toDto);
    }

    @KafkaListener(
            topics = "user-creation",
            containerFactory = "userKafkaListenerContainerFactory",
            groupId = "chat")
    public void listenKafka(User user) {
        userRepo.save(USER_MAPPER.toEntity(user))
                .doOnError(error -> log.error("An error occurred while saving received user: {}", user, error))
                .subscribe(savedUser -> log.info("Received and saved User in group chat: {} ", savedUser));
    }
}
