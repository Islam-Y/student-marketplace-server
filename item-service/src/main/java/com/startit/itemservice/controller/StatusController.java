package com.startit.itemservice.controller;

import com.startit.itemservice.service.StatusService;
import com.startit.itemservice.transfer.Status;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/status")
@RequiredArgsConstructor
public class StatusController {

    private final StatusService service;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody Status status) {
        if (status.getId() != null)
            return ResponseEntity.badRequest().body("Указано лишнее поле : id!");
        if (status.getName() == null)
            return ResponseEntity.badRequest().body("Пропущено обязательное поле : name!");
        if (status.getDescription() == null)
            return ResponseEntity.badRequest().body("Пропущено обязательное поле : description!");

        try {
            status.setId(service.save(status));
            return ResponseEntity.ok(status);
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
