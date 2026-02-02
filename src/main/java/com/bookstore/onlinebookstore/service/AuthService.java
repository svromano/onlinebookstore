package com.bookstore.onlinebookstore.service;

import com.bookstore.onlinebookstore.entity.User;
import com.bookstore.onlinebookstore.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * The type Auth service.
 */
@Service
public class AuthService {

    // Final fields ensure immutability and allow for constructor-based dependency injection
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Instantiates a new Auth service.
     *
     * @param userRepository  the user repository
     * @param passwordEncoder the password encoder
     */
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Register user.
     *
     * @param user the user
     * @return the user
     */
    public User register(User user) {

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }
}