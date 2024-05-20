package com.startit.userservice.controller;

import com.startit.userservice.service.UserService;
import com.startit.shared.transfer.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/save")
    public ResponseEntity<Object> saveUser(@RequestBody User user) {
        try {
            Long id = service.save(user);
            log.info("User {} saved", user.getUsername());
            return ResponseEntity.ok(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> getInfo(@PathVariable Long id) {
        return service.findById(id)
                .map(user -> ResponseEntity.ok(user.orElseThrow()))
                .onErrorReturn(Exception.class, ResponseEntity.badRequest().build());
    }

    @GetMapping("/username/{username}")
    public Mono<ResponseEntity<Optional<User>>> userByUsername(@PathVariable String username) {
        return service.findByUsername(username)
                .map(ResponseEntity::ok)
                .onErrorReturn(Exception.class, ResponseEntity.internalServerError().build());
    }

    @GetMapping("/exists/{id}")
    public Mono<ResponseEntity<Boolean>> userExists(@PathVariable Long id) {
        return service.findById(id)
                .map(user -> ResponseEntity.ok(user.isPresent()))
                .onErrorReturn(Exception.class, ResponseEntity.internalServerError().build());
    }
}
