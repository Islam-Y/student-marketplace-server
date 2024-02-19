package com.startit.authservice.service;

import com.startit.authservice.transfer.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping(value = "/api/v1/user/username/{username}")
    Optional<User> findByUsername(@PathVariable String username);
    @PostMapping(value = "/api/v1/user/save")
    Long save(@RequestBody User user);
}
