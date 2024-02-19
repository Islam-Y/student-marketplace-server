package com.startit.itemservice.controller;

import com.startit.itemservice.service.ItemService;
import com.startit.itemservice.transfer.Item;
import com.startit.itemservice.transfer.SearchFilter;
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

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody Item item) {
        if (item.getId() != null)
            return ResponseEntity.badRequest().body("Указано лишнее поле : id!");
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

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestBody SearchFilter searchFilter,
                                                 Pageable pageable,
                                                 HttpServletResponse response
    ) {
        try {
            var result = service.searchItems(searchFilter, pageable);
            response.setHeader("X-Total-Count", String.valueOf(result.size()));
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(Pageable pageable,
                                         HttpServletResponse response
    ) {
        try {
            var result = service.getAll(pageable);
            response.setHeader("X-Total-Count", String.valueOf(result.size()));
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getInfo(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.findById(id).orElseThrow());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/all_by_user/{id}")
    public ResponseEntity<Page<Item>> getItemsByUser(@PathVariable Long id, Pageable pageable) {
        try {
            return ResponseEntity.ok(service.findBySellerId(id, pageable));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
