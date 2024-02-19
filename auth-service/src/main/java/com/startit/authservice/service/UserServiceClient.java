package com.startit.authservice.service;

import com.startit.authservice.transfer.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping(value = "/api/v1/user/username/{username}")
    User findByUsername(@PathVariable String username);
    @GetMapping(value = "/api/v1/user")
    Long save(@RequestBody User user);
}
