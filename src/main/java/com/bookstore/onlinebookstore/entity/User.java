package com.bookstore.onlinebookstore.entity;

import jakarta.persistence.*;
import lombok.*;


/**
 * The type User.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    // Unique primary key for each user, automatically incremented by the database
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The unique login name; 'nullable = false' and 'unique = true' ensure
    // every user has a name and no two users share the same one.
    @Column(nullable = false, unique = true)
    private String username;

    // Used for communication and password recovery; also restricted to be unique.
    @Column(nullable = false, unique = true)
    private String email;

    // Stores the user's password. In a real application, this contains a
    // BCrypt hash, never the actual plain-text password, for security.
    @Column(nullable = false)
    private String password;
}