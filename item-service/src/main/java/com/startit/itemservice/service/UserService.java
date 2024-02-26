package com.startit.itemservice.service;

import com.startit.itemservice.transfer.User;
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

    public Optional<User> findFallbackByUsername(String username, Throwable throwable) {
        return Optional.empty();
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "userFallbackExists")
    public Boolean userExists(Long id) {
        return userServiceClient.userExists(id);
    }

    public Boolean userFallbackExists(Long id, Throwable throwable) {
        return false;
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "getFallbackUser")
    public Optional<User> getUser(Long id) {
        return userServiceClient.getUser(id);
    }

    public Optional<User> getFallbackUser(Long id, Throwable throwable) {
        return Optional.empty();
    }
}
