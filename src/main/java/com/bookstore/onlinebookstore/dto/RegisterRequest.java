package com.bookstore.onlinebookstore.dto;

/**
 * A Data Transfer Object (DTO) used to capture new user details.
 * This record acts as a secure, immutable "data package" sent from the
 * registration form to the backend.
 */
public record RegisterRequest(
        // The unique handle the user chooses for their account
        String username,

        // The contact address used for notifications and account identity
        String email,

        // The raw password provided by the user (which will be hashed by the Service)
        String password
) {
    // Records automatically provide: constructor, getters (username(), etc.),
    // and boilerplate methods like toString() and equals().
}