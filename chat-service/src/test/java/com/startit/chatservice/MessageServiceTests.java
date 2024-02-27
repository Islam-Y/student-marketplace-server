package com.startit.chatservice;

import com.startit.chatservice.entity.MessageEntity;
import com.startit.chatservice.mapper.ChatMapper;
import com.startit.chatservice.repository.ChatRepo;
import com.startit.chatservice.repository.MessageRepo;
import com.startit.chatservice.service.MessageService;
import com.startit.chatservice.service.UserService;
import com.startit.chatservice.transfer.Chat;
import com.startit.chatservice.transfer.Message;
import com.startit.chatservice.transfer.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

//@SpringBootTest
//@RunWith(SpringRunner.class)
public class MessageServiceTests {

    @Mock
    MessageRepo repo;

    @Mock
    ChatRepo chatRepo;

    @Mock
    UserService userService;

    @InjectMocks
    MessageService messageService;

    private static final ChatMapper MAPPER = ChatMapper.INSTANCE;

//    @Test
    public void testSaveMessage() {
        // Arrange
        Long chatId = 1L;
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(chatId);
        User sender = new User();
        sender.setId(123L);
        MessageEntity messageEntity = new MessageEntity();

        when(chatRepo.findById(chatId)).thenReturn(Mono.just(MAPPER.toEntity(chat)));
        when(userService.getUser(message.getSenderId())).thenReturn(Optional.of(sender));
        when(repo.save(messageEntity)).thenReturn(Mono.just(messageEntity));

        // Act
        Mono<Message> savedMessageMono = messageService.save(chatId, message);
        Message savedMessage = savedMessageMono.block();

        // Assert
        assertNotNull(savedMessage);
    }
}
