package com.startit.chatservice.service;

import com.startit.chatservice.transfer.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping(value = "/api/v1/user/{id}")
    User getUser(@PathVariable Long id);
}
