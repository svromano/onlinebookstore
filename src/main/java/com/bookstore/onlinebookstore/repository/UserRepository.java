package com.bookstore.onlinebookstore.repository;

import com.bookstore.onlinebookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Data Access Layer for User Accounts.
 * This repository is vital for Authentication (Login) and Authorization (Permissions).
 * It provides built-in tools to verify if a user exists before allowing access.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    // Used during Login: Returns an 'Optional' to handle cases where
    // the username might not exist, preventing null pointer errors.
    Optional<User> findByUsername(String username);

    // Used during Registration: Checks if a username is already taken.
    boolean existsByUsername(String username);

    // Used during Registration: Checks if an email is already in use
    // to prevent duplicate account creation.
    boolean existsByEmail(String email);
}