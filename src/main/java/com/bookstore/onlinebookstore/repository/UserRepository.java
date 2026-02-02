package com.bookstore.onlinebookstore.repository;

import com.bookstore.onlinebookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


/**
 * The interface User repository.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find by username optional.
     *
     * @param username the username
     * @return the optional
     */

    Optional<User> findByUsername(String username);

    /**
     * Exists by username boolean.
     *
     * @param username the username
     * @return the boolean
     */

    boolean existsByUsername(String username);

    /**
     * Exists by email boolean.
     *
     * @param email the email
     * @return the boolean
     */
    boolean existsByEmail(String email);
}