package com.startit.userservice.controller;

import com.startit.userservice.service.ChatServiceClient;
import com.startit.userservice.service.UserService;
import com.startit.userservice.transfer.Chat;
import com.startit.userservice.transfer.User;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final ChatServiceClient chatService;

    @GetMapping("/chats")
    public Mono<ResponseEntity<List<Chat>>> getChats(Pageable pageable,
                                                     HttpServletResponse response,
                                                     String username) {
        return service.findByUsername(username)
                .map(Optional::orElseThrow)
                .map(user -> chatService.getChatsByUserId(user.getId(), pageable))
                .map(chats -> {
                    response.setHeader("X-Total-Count", String.valueOf(chats.size()));
                    return ResponseEntity.ok(chats);
                })
                .onErrorReturn(Exception.class, ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> getInfo(@PathVariable Long id) {
        return service.findById(id)
                .map(user -> ResponseEntity.ok(user.orElseThrow()))
                .onErrorReturn(Exception.class, ResponseEntity.badRequest().build());
    }

    @GetMapping("/exists/{id}")
    public Mono<ResponseEntity<Boolean>> userExists(@PathVariable Long id) {
        return service.findById(id)
                .map(user -> ResponseEntity.ok(user.isPresent()))
                .onErrorReturn(Exception.class, ResponseEntity.internalServerError().build());
    }
}
