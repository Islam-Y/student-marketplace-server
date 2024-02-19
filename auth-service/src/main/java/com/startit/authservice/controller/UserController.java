package com.startit.authservice.controller;

import com.startit.authservice.service.ChatServiceClient;
import com.startit.authservice.service.UserService;
import com.startit.authservice.transfer.User;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    private final ChatServiceClient chatService;

    @GetMapping("/chats")
    public ResponseEntity<Object> getChats(Pageable pageable,
                                               HttpServletResponse response,
                                               Authentication authentication
    ) {
        try {
            var user = service.findByUsername(authentication.getName());
            if (user.isEmpty())
                return ResponseEntity.ok(Collections.EMPTY_LIST);

            var result = chatService.getChatsByUserId(user.get().getId(), pageable);
            response.setHeader("X-Total-Count", String.valueOf(result.size()));
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getInfo(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.findById(id).orElseThrow());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> userExists(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.findById(id).isPresent());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
