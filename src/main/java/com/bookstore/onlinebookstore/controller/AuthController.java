package com.bookstore.onlinebookstore.controller;

import com.bookstore.onlinebookstore.entity.User;
import com.bookstore.onlinebookstore.repository.UserRepository;
import com.bookstore.onlinebookstore.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email already registered");
        }

        authService.register(user);
        return ResponseEntity.ok("User registered successfully");
    }
}
