package com.startit.chatservice.service;

import com.startit.chatservice.transfer.User;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserServiceClient userServiceClient;

    @CircuitBreaker(name = "userService", fallbackMethod = "getFallbackUser")
    public Optional<User> getUser(@PathVariable Long id) {
        return userServiceClient.getUser(id);
    }

    public Optional<User> getFallbackUser(@PathVariable Long id, Throwable throwable) {
        return Optional.empty();
    }
}
