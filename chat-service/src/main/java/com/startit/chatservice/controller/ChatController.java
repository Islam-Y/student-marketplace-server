package com.startit.chatservice.controller;

import com.startit.chatservice.service.ChatService;
import com.startit.chatservice.service.MessageService;
import com.startit.chatservice.transfer.Chat;
import com.startit.chatservice.transfer.Message;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final ChatService service;
    private final MessageService messageService;

    @PostMapping("/create")
    public Mono<ResponseEntity<Chat>> create(@RequestBody Chat chat) {
        chat.setId(null);
        return service.save(chat)
                .map(ResponseEntity::ok)
                .onErrorReturn(IllegalArgumentException.class, ResponseEntity.badRequest().build());
    }

    @PostMapping("/sendMessage")
    public ResponseEntity<Object> processMessage(@RequestBody Message message) {
        try {
            Message savedMessage =  messageService.save(message);
            return ResponseEntity.ok(savedMessage);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/messages/{chatId}")
    public Flux<Message> getMessages(@PathVariable Long chatId, Pageable pageable) {
        return messageService.getMessages(chatId, pageable);
    }

    @GetMapping("/pollMessages")
    public ResponseEntity<Object[]> getNewMessages(
            @RequestParam Long userId,
            @RequestParam Long lastMessageId) {
        try {
            return ResponseEntity.ok(messageService.getNewMessages(userId, lastMessageId).toArray());
        } catch (InterruptedException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @CircuitBreaker(name = "getUserChats", fallbackMethod = "getUserChatsFallback")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Object> getUserChats(@PathVariable Long userId, Pageable pageable, HttpServletResponse response) {
        List<Chat> chats = service.getUserChats(userId, pageable);
        response.setHeader("X-Total-Count", String.valueOf(chats.size()));
        return ResponseEntity.ok(chats);
    }

    private Mono<ResponseEntity<Object>> getUserChatsFallback(Throwable t) {
        return Mono.just(ResponseEntity.badRequest()
                .body("Unable to get user chats. Try again later")
        );
    }
}

