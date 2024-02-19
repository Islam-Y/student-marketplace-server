package com.startit.userservice.controller;

import com.startit.userservice.service.ChatServiceClient;
import com.startit.userservice.service.UserService;
import com.startit.userservice.transfer.Chat;
import com.startit.userservice.transfer.User;
import feign.Body;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final ChatServiceClient chatService;

    @PostMapping("/save")
    public Mono<ResponseEntity<Long>> saveUser(@RequestBody User user) {
        return service.save(user)
                .map(ResponseEntity::ok);
    }

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

    @GetMapping("/username/{username}")
    public Mono<ResponseEntity<Optional<User>>> userByUsername(@PathVariable String username) {
        return service.findByUsername(username)
                .map(ResponseEntity::ok)
                .onErrorReturn(Exception.class, ResponseEntity.internalServerError().build());
    }

    @GetMapping("/exists/{id}")
    public Mono<ResponseEntity<Boolean>> userExists(@PathVariable Long id) {
        return service.findById(id)
                .map(user -> ResponseEntity.ok(user.isPresent()))
                .onErrorReturn(Exception.class, ResponseEntity.internalServerError().build());
    }
}
