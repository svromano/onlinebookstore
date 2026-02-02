package com.bookstore.onlinebookstore.controller;

import com.bookstore.onlinebookstore.entity.User;
import com.bookstore.onlinebookstore.repository.UserRepository;
import com.bookstore.onlinebookstore.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The type Auth controller.
 */
@RestController
@RequestMapping("/api/auth") // Base URL path for all authentication-related endpoints
@RequiredArgsConstructor     // Automatically injects required services (authService, userRepository)
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    /**
     * Register response entity.
     *
     * @param user the user
     * @return the response entity
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {

        // Validation Check 1: Ensure the username isn't already taken
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        // Validation Check 2: Ensure the email isn't already in use
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email already registered");
        }

        // If checks pass, delegate the password hashing and saving to the Service layer
        authService.register(user);

        return ResponseEntity.ok("User registered successfully");
    }
}