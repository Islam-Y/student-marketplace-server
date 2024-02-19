package com.startit.chatservice.service;

import com.startit.chatservice.transfer.Item;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

@FeignClient(name = "item-service")
public interface ItemServiceClient {
    @GetMapping(value = "/api/v1/item/all_by_user/{id}")
    Page<Item> getItemByUser(@PathVariable Long id, Pageable pageable);
}
