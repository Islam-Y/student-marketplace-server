package com.startit.chatservice;

import com.startit.chatservice.entity.ChatEntity;
import com.startit.chatservice.repository.ChatRepo;
import com.startit.chatservice.service.ChatService;
import com.startit.chatservice.service.ItemService;
import com.startit.chatservice.transfer.Chat;
import com.startit.chatservice.transfer.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ChatServiceTests {

    @Mock
    ChatRepo repo;

    @Mock
    ItemService itemService;

    @InjectMocks
    ChatService chatService;

    @Test
    public void testSaveChat() {
        // Arrange
        Chat chat = new Chat();
        ChatEntity chatEntity = new ChatEntity();
        when(repo.save(chatEntity)).thenReturn(Mono.just(chatEntity));

        // Act
        Mono<Chat> savedChatMono = chatService.save(chat);
        Chat savedChat = savedChatMono.block();

        // Assert
        assertNotNull(savedChat);
    }

    @Test
    public void testGetUserChats() {
        // Arrange
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Item item = new Item();
        item.setId(1L);
        ChatEntity chatEntity = new ChatEntity();
        List<Item> userItems = List.of(item);

        when(itemService.getItemByUser(userId, pageable)).thenReturn(new PageImpl<>(userItems));
        when(repo.findByItemId(1L, pageable)).thenReturn(Flux.just(chatEntity));
        when(repo.findByCustomerId(userId, pageable)).thenReturn(Flux.just(chatEntity));

        // Act
        Flux<Chat> userChatsFlux = chatService.getUserChats(userId, pageable);
        List<Chat> userChats = userChatsFlux.collectList().block();

        // Assert
        assertNotNull(userChats);
        assertEquals(1, userChats.size());
    }
}
