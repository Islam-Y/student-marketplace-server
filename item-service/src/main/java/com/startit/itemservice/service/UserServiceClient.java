package com.startit.itemservice.service;

import com.startit.itemservice.transfer.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping(value = "/api/v1/user/exists/{id}")
    public Boolean userExists(@PathVariable Long id);
    @GetMapping(value = "/api/v1/user/{id}")
    public User getUser(@PathVariable Long id);
}
