package com.bookstore.onlinebookstore.dto;

/**
 * The type Login request.
 */
public record LoginRequest(
        String username,
        String password
) {
}