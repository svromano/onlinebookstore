package com.bookstore.onlinebookstore.dto;

public record LoginRequest(
        String username,
        String password
) {
}
