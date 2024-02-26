package com.startit.itemservice.service;

import com.startit.itemservice.transfer.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping(value = "/api/v1/user/username/{username}")
    Optional<User> findByUsername(@PathVariable String username);
    @GetMapping(value = "/api/v1/user/exists/{id}")
    Boolean userExists(@PathVariable Long id);
    @GetMapping(value = "/api/v1/user/{id}")
    Optional<User> getUser(@PathVariable Long id);
}
