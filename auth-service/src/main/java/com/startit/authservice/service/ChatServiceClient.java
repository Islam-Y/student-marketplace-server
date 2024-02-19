package com.startit.authservice.service;

import com.startit.authservice.transfer.Chat;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "chat-service")
public interface ChatServiceClient {
    @GetMapping(value = "/api/v1/chat/by_user/{id}")
    List<Chat> getChatsByUserId(Long userId, Pageable page);
}
