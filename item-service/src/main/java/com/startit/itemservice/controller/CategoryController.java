package com.startit.itemservice.controller;

import com.startit.itemservice.service.CategoryService;
import com.startit.itemservice.transfer.Category;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody Category category) {
        if (category.getId() != null)
            return ResponseEntity.badRequest().body("Указано лишнее поле : id!");
        if (category.getName() == null)
            return ResponseEntity.badRequest().body("Пропущено обязательное поле : name!");
        if (category.getDescription() == null)
            return ResponseEntity.badRequest().body("Пропущено обязательное поле : description!");

        try {
            category.setId(service.save(category));
            return ResponseEntity.ok(category);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(Pageable pageable,
                                         HttpServletResponse response) {
        var result = service.getAll(pageable);
        response.addHeader("X-Total-Count", String.valueOf(result.getTotalElements()));
        return ResponseEntity.ok(result.toList());
    }
}
