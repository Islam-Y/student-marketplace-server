package com.startit.chatservice.service;

import com.startit.chatservice.transfer.Item;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "item-service")
public interface ItemServiceClient {
    @GetMapping("/api/v1/item/search")
    List<Item> searchItem(@RequestParam("sellerId") Long sellerId, Pageable pageable);

    @GetMapping(value = "/api/v1/item/getInfo?itemId={id}")
    Optional<Item> getItem(@PathVariable Long id);
}
