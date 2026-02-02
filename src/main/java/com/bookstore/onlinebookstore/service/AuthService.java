package com.bookstore.onlinebookstore.service;

import com.bookstore.onlinebookstore.entity.User;
import com.bookstore.onlinebookstore.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for handling authentication and user registration logic.
 * The @Service annotation registers this class as a Spring Bean in the application context.
 */
@Service
public class AuthService {

    // Final fields ensure immutability and allow for constructor-based dependency injection
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor Injection: Spring automatically provides the UserRepository
     * and the PasswordEncoder bean (likely BCrypt) defined in your SecurityConfig.
     */
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user by securing their credentials and persisting them to the database.
     * * @param user The user entity containing raw registration data (username, email, plain password).
     * @return The saved User entity, now containing the encoded password and generated ID.
     */
    public User register(User user) {
        // 1. Hash the plain-text password provided by the user.
        // This prevents storing "plainPassword123" in the database, which is a major security risk.
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        // 2. Overwrite the user's plain-text password with the secure, hashed version.
        user.setPassword(encodedPassword);

        // 3. Persist the user to the MySQL database via the JPA repository.
        // This is where the 'INSERT INTO users...' SQL command is actually triggered.
        return userRepository.save(user);
    }
}