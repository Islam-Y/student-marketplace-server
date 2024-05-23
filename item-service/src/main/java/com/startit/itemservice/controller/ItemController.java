package com.startit.itemservice.controller;

import com.startit.itemservice.jwt.JwtService;
import com.startit.itemservice.service.ItemService;
import com.startit.itemservice.transfer.Item;
import com.startit.itemservice.transfer.SearchFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;
    private final JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody Item item) {
        if (item.getName() == null)
            return ResponseEntity.badRequest().body("Пропущено обязательное поле : name!");
        if (item.getPrice() == null)
            return ResponseEntity.badRequest().body("Пропущено обязательное поле : price!");
        if (item.getDescription() == null)
            return ResponseEntity.badRequest().body("Пропущено обязательное поле : description!");
        if (item.getStatusId() == null)
            return ResponseEntity.badRequest().body("Пропущено обязательное поле : statusId!");
        if (item.getLocationId() == null)
            return ResponseEntity.badRequest().body("Пропущено обязательное поле : locationId!");
        if (item.getCategoriesIds() == null)
            return ResponseEntity.badRequest().body("Пропущено обязательное поле : categoriesIds!");
        if (item.getSellerId() == null)
            return ResponseEntity.badRequest().body("Пропущено обязательное поле : sellerId!");

        try {
            item.setId(service.save(item));
            return ResponseEntity.ok(item);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Object> update(@RequestBody Item item,
                                         HttpServletRequest request) {
        var username = jwtService.extractUsername(
                request.getHeader("Authorization").substring(7)
        );
        if (item.getId() == null)
            return ResponseEntity.badRequest().body("Пропущено обязательное поле : id!");
        if (item.getName() == null)
            return ResponseEntity.badRequest().body("Пропущено обязательное поле : name!");
        if (item.getPrice() == null)
            return ResponseEntity.badRequest().body("Пропущено обязательное поле : price!");
        if (item.getDescription() == null)
            return ResponseEntity.badRequest().body("Пропущено обязательное поле : description!");
        if (item.getStatusId() == null)
            return ResponseEntity.badRequest().body("Пропущено обязательное поле : statusId!");
        if (item.getLocationId() == null)
            return ResponseEntity.badRequest().body("Пропущено обязательное поле : locationId!");
        if (item.getCategoriesIds() == null)
            return ResponseEntity.badRequest().body("Пропущено обязательное поле : categoriesIds!");
        if (item.getSellerId() == null)
            return ResponseEntity.badRequest().body("Пропущено обязательное поле : sellerId!");

        try {
            service.update(item, username);
            return ResponseEntity.ok(item);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/getInfo")
    public ResponseEntity<Object> getItem(
            Long itemId
    ) {
        try {
            return ResponseEntity.ok(service.searchItem(itemId));
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(
            String itemName,
            Long categoryId,
            Long locationId,
            Long sellerId,
            Pageable pageable,
            HttpServletResponse response
    ) {
        try {
            SearchFilter searchFilter = new SearchFilter();
            searchFilter.setItemName(itemName);
            searchFilter.setCategoryId(categoryId);
            searchFilter.setLocationId(locationId);
            searchFilter.setSellerId(sellerId);
            var result = service.searchItems(searchFilter, pageable);
            response.setHeader("X-Total-Count", String.valueOf(result.size()));
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
