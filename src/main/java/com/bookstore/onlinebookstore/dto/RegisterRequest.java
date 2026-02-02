package com.bookstore.onlinebookstore.dto;

/**
 * The type Register request.
 */
public record RegisterRequest(
        // The unique handle the user chooses for their account
        String username,

        // The contact address used for notifications and account identity
        String email,

        // The raw password provided by the user (which will be hashed by the Service)
        String password
) {
}