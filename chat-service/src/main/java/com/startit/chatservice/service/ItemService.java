package com.startit.chatservice.service;

import com.startit.chatservice.transfer.Item;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemServiceClient itemServiceClient;

    @CircuitBreaker(name = "itemService", fallbackMethod = "getFallbackItemByUser")
    public Page<Item> getItemByUser(@PathVariable Long id, Pageable pageable) {
        return itemServiceClient.getItemByUser(id, pageable);
    }

    public Page<Item> getFallbackItemByUser(@PathVariable Long id, Pageable pageable, Throwable throwable) {
        return Page.empty();
    }

    @CircuitBreaker(name = "itemService", fallbackMethod = "getFallbackItemByUser")
    public Optional<Item> getItem(@PathVariable Long id) {
        return itemServiceClient.getItem(id);
    }

    public Optional<Item> getFallbackItem(@PathVariable Long id, Throwable throwable) {
        return Optional.empty();
    }
}
