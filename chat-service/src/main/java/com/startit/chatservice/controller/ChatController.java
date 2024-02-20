package com.startit.chatservice.controller;

import com.startit.chatservice.service.ChatService;
import com.startit.chatservice.service.MessageService;
import com.startit.chatservice.transfer.Chat;
import com.startit.chatservice.transfer.Message;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService service;
    private final MessageService messageService;

    @PostMapping("/create")
    public Mono<ResponseEntity<Chat>> create(@RequestBody Chat chat) {
        return service.save(chat)
                .map(ResponseEntity::ok)
                .onErrorReturn(IllegalArgumentException.class, ResponseEntity.badRequest().build());
    }

    @CircuitBreaker(name = "postMessage", fallbackMethod = "postMessageFallback")
    @PostMapping("/{chatId}/post")
    public Mono<ResponseEntity<Object>> postMessage(@PathVariable Long chatId,
                                                    @RequestBody Message message) {
        return messageService.save(chatId, message)
                .map(ResponseEntity::ok);
    }

    public Mono<ResponseEntity<Object>> postMessageFallback(Exception e) {
        return Mono.just(ResponseEntity.badRequest()
                .body("Unable to post message. Try again later")
        );
    }

    @GetMapping("/{chatId}/messages")
    public Flux<Message> getMessages(@PathVariable Long chatId, Pageable pageable) {
        return messageService.getMessages(chatId, pageable);
    }

    @CircuitBreaker(name = "getUserChats", fallbackMethod = "getUserChatsFallback")
    @GetMapping("/user_chats")
    public Mono<ResponseEntity<Object>> getUserChats(Pageable pageable,
                                                         HttpServletResponse response,
                                                         Long id) {
        return service.getUserChats(id, pageable)
                .collectList()
                .doOnNext(chats -> response.setHeader("X-Total-Count", String.valueOf(chats.size())))
                .map(ResponseEntity::ok);
    }

    private Mono<ResponseEntity<Object>> getUserChatsFallback(Throwable t) {
        return Mono.just(ResponseEntity.badRequest()
                .body("Unable to get user chats. Try again later")
        );
    }
}

