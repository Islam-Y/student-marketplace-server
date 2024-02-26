package com.startit.authservice.service;

import com.startit.authservice.transfer.User;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserServiceClient userServiceClient;

    @CircuitBreaker(name = "userService", fallbackMethod = "findFallbackByUsername")
    public Optional<User> findByUsername(String username) {
        return userServiceClient.findByUsername(username);
    }

    /* Main Idea to prevent transfer access token when auth-service down */
    public Optional<User> findFallbackByUsername(String username, Throwable throwable) {
        return Optional.empty();
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "saveFallback")
    public Long save(User user) {
        return userServiceClient.save(user);
    }

    public Long saveFallback(User user, Throwable throwable) {
        return 0L;
    }
}
