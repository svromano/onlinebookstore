package com.bookstore.onlinebookstore.dto;

public record RegisterRequest(
        String username,
        String email,
        String password
) {
}
