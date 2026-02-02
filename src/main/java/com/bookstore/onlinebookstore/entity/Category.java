package com.bookstore.onlinebookstore.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * This class is a "Database Blueprint" (JPA Entity).
 * It tells Spring Boot how to map Java objects to a MySQL table named 'categories'.
 * * 1. LOMBOK (@Data): Automatically handles the boilerplate code (getters/setters).
 * 2. PERSISTENCE (@Entity/@Table): Defines this class as a database-managed object.
 * 3. IDENTITY (@Id/@GeneratedValue): Ensures every category has a unique,
 * auto-incrementing ID so we can distinguish "Fiction" from "Java" books.
 */
@Data
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}