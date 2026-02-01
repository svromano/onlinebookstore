package com.bookstore.onlinebookstore.service;

import com.bookstore.onlinebookstore.entity.User;
import com.bookstore.onlinebookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    // If using password encryption, inject PasswordEncoder here

    public User register(User user) {
        // If using BCrypt, hash the password:
        // user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }
}