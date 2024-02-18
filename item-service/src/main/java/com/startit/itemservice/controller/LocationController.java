package com.startit.itemservice.controller;

import com.startit.itemservice.service.LocationService;
import com.startit.itemservice.transfer.Location;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService service;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody Location location) {
        if (location.getId() != null)
            return ResponseEntity.badRequest().body("Указано лишнее поле : id!");
        if (location.getName() == null)
            return ResponseEntity.badRequest().body("Пропущено обязательное поле : name!");
        if (location.getDescription() == null)
            return ResponseEntity.badRequest().body("Пропущено обязательное поле : description!");

        try {
            location.setId(service.save(location));
            return ResponseEntity.ok(location);
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
