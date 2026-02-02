package com.bookstore.onlinebookstore.dto;

/**
 * A Data Transfer Object (DTO) used specifically for user login.
 * Using a 'record' makes this class immutable and concise.
 * * 1. IMMUTABILITY: Data cannot be changed once the request is received, making it thread-safe.
 * 2. BOILERPLATE-FREE: Java automatically generates the constructor, getters,
 * equals(), and hashCode() methods in one line.
 * 3. SECURITY: It carries only the essential credentials (username/password)
 * from the login form to the authentication service.
 */
public record LoginRequest(
        String username,
        String password
) {
}