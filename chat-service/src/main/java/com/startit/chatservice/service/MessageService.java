package com.startit.chatservice.service;

import com.startit.chatservice.entity.ChatEntity;
import com.startit.chatservice.entity.MessageEntity;
import com.startit.chatservice.entity.UserEntity;
import com.startit.chatservice.entity.UserMessageSequence;
import com.startit.chatservice.mapper.MessageMapper;
import com.startit.chatservice.mapper.UserMapper;
import com.startit.chatservice.repository.ChatRepo;
import com.startit.chatservice.repository.MessageRepo;
import com.startit.chatservice.repository.UserMessageSequenceRepo;
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
import reactor.util.function.Tuples;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class MessageService {

    private final MessageRepo repo;
    private final ChatRepo chatRepo;
    private final UserRepo userRepo;
    private final UserMessageSequenceRepo personalSequenceRepo;
    private final ItemService itemService;
    private final BlockingQueue<UserMessageSequence> messageQueue = new LinkedBlockingQueue<>();

    private static final MessageMapper MAPPER = MessageMapper.INSTANCE;
    private static final UserMapper USER_MAPPER = UserMapper.INSTANCE;

    public Message save(Message message) {
        ChatEntity chatEntity = chatRepo.findById(message.getChatId()).block();
        UserEntity user = userRepo.findById(message.getSenderId()).block();

        MessageEntity messageEntity = MAPPER.toEntity(message);
        messageEntity.setChatId(chatEntity.getId());
        messageEntity.setSenderId(user.getId());
        MessageEntity savedMessageEntity = repo.save(messageEntity).block();

        Long sellerId = itemService.getItem(chatEntity.getItemId())
                .orElseThrow().getSellerId();
        Long customerId = chatEntity.getCustomerId();

        Long sellerSeqNumber = saveNewSequentialMessage(sellerId, savedMessageEntity);
        Long customerSeqNumber = saveNewSequentialMessage(customerId, savedMessageEntity);

        Message newMessage = MAPPER.toDto(savedMessageEntity);
        if (Objects.equals(savedMessageEntity.getSenderId(), sellerId)) {
            newMessage.setSeqNumber(sellerSeqNumber);
        } else {
            newMessage.setSeqNumber(customerSeqNumber);
        }

        return newMessage;
    }

    public Flux<Message> getMessages(Long chatId, Pageable pageable) {
        return repo.findByChatId(chatId, pageable)
                .map(MAPPER::toDto);
    }

    public List<Message> getNewMessages(Long userId, Long lastMessageId) throws InterruptedException {
        List<UserMessageSequence> newMessages = personalSequenceRepo
                .findByUserIdAndMessageIdGreaterThanOrderByMessageId(userId, lastMessageId)
                .collectList()
                .block();

        if (newMessages == null || newMessages.isEmpty()) {
            UserMessageSequence newMessage = messageQueue.poll(3, TimeUnit.SECONDS);
            if (newMessage != null) {
                newMessages = personalSequenceRepo
                        .findByUserIdAndMessageIdGreaterThanOrderByMessageId(userId, lastMessageId)
                        .collectList()
                        .block();

                if (newMessages == null) {
                    return List.of();
                }
                return newMessages
                        .stream()
                        .map(userMessageSequence -> {
                            Message message = MAPPER.toDto(repo.findById(userMessageSequence.getMessageId()).block());
                            message.setSeqNumber(userMessageSequence.getSeqNumber());
                            return message;
                        })
                        .collect(Collectors.toList());
            }
        }

        if (newMessages == null) {
            return List.of();
        }

        return newMessages
                .stream()
                .map(userMessageSequence -> {
                    Message message = MAPPER.toDto(repo.findById(userMessageSequence.getMessageId()).block());
                    message.setSeqNumber(userMessageSequence.getSeqNumber());
                    return message;
                })
                .collect(Collectors.toList());
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

    private Long saveNewSequentialMessage(Long userId, MessageEntity messageEntity) {
        Optional<UserMessageSequence> customerLastMessage = personalSequenceRepo
                .findTopByUserIdOrderBySeqNumberDesc(userId).blockOptional();
        UserMessageSequence newCustomerMessage = new UserMessageSequence();
        newCustomerMessage.setUserId(userId);
        newCustomerMessage.setMessageId(messageEntity.getId());
        if (customerLastMessage.isEmpty()) {
            newCustomerMessage.setSeqNumber(0L);
        } else {
            newCustomerMessage.setSeqNumber(customerLastMessage.get().getSeqNumber() + 1);
        }
        return personalSequenceRepo.save(newCustomerMessage).block().getSeqNumber();
    }

}
