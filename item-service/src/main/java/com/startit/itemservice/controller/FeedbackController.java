package com.startit.itemservice.controller;

import com.startit.itemservice.jwt.JwtService;
import com.startit.itemservice.service.FeedbackService;
import com.startit.itemservice.transfer.Feedback;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService service;

    private final JwtService jwtService;

    @CircuitBreaker(name = "createFeedback", fallbackMethod = "createFeedbackFallback")
    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody Feedback feedback,
                                         HttpServletRequest request) {
        var username = jwtService.extractUsername(
                request.getHeader("Authorization").substring(7)
        );
        log.info("Create feedback by {}!", username);

        if (feedback.getMark() == null)
            return ResponseEntity.badRequest().body("Пропущено обязательное поле : mark!");
        if (feedback.getItemId() == null)
            return ResponseEntity.badRequest().body("Пропущено обязательное поле : itemId!");

        return ResponseEntity.ok(service.save(username, feedback));
    }

    private ResponseEntity<Object> createFeedbackFallback(Throwable t) {
        // Consider logging or sending alerts about the fallback event
        return ResponseEntity.status(500).body(t.getMessage());
    }

    @GetMapping("/seller/{userId}")
    public ResponseEntity<List<Feedback>> getSellerFeedback(@PathVariable Long userId,
                                                            Pageable pageable,
                                                            HttpServletResponse response
    ) {
        try {
            var result = service.getSellerFeedback(userId, pageable);
            response.addHeader("X-Total-Count", String.valueOf(result.size()));
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/customer/{userId}")
    public ResponseEntity<List<Feedback>> getCustomerFeedback(@PathVariable Long userId,
                                                              Pageable pageable,
                                                              HttpServletResponse response
    ) {
        try {
            var result = service.getCustomerFeedback(userId, pageable);
            response.addHeader("X-Total-Count", String.valueOf(result.size()));
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
