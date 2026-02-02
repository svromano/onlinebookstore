package com.bookstore.onlinebookstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity class representing the 'books' table in the database.
 * @Entity: Specifies that this class is a JPA entity.
 * @Table: Maps this entity to the specific table name in MySQL.
 */
@Entity
@Table(name = "books")
@Getter // Lombok: Generates all getter methods
@Setter // Lombok: Generates all setter methods
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key with Auto-Increment

    private String title;

    private String author;

    private Double price;

    // Specifies that this string can hold a long block of text (SQL TEXT type)
    // rather than a standard VARCHAR(255).
    @Column(columnDefinition = "TEXT")
    private String description;

    private String imageUrl;

    /**
     * Relationship Mapping:
     * @ManyToOne: Many books can belong to one category.
     * fetch = FetchType.LAZY: Don't load the category data unless it's explicitly called
     * (improves performance).
     * @JoinColumn: Defines the foreign key column name in the 'books' table.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}