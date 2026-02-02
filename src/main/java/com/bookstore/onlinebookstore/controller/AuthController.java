package com.bookstore.onlinebookstore.controller;

import com.bookstore.onlinebookstore.entity.User;
import com.bookstore.onlinebookstore.repository.UserRepository;
import com.bookstore.onlinebookstore.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The Authentication Controller handles incoming web requests for user security.
 * It acts as the "Gatekeeper," processing registration and login attempts via REST API.
 */
@RestController
@RequestMapping("/api/auth") // Base URL path for all authentication-related endpoints
@RequiredArgsConstructor     // Automatically injects required services (authService, userRepository)
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    /**
     * POST /api/auth/register
     * Receives a User object from the frontend and performs validation checks
     * before saving them to the database.
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