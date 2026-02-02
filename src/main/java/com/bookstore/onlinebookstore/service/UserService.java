package com.bookstore.onlinebookstore.service;

import com.bookstore.onlinebookstore.dto.RegisterRequest;
import com.bookstore.onlinebookstore.entity.User;
import com.bookstore.onlinebookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for user-related operations.
 * Focuses on business rules for account creation and management.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user account after validating uniqueness.
     * @param request The Record (DTO) containing registration details from the frontend.
     * @throws RuntimeException if the username or email is already taken.
     */
    public void register(RegisterRequest request) {

        // 1. Validation: Check if the username is already in use
        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Username already exists");
        }

        // 2. Validation: Check if the email is already in use
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        // 3. Transformation: Map the DTO data to a new User Entity
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());

        // 4. Security: Hash the password before it ever touches the database
        // request.password() is a record getter
        user.setPassword(passwordEncoder.encode(request.password()));

        // 5. Persistence: Save the new user to the 'users' table
        userRepository.save(user);
    }
}