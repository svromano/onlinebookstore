package com.bookstore.onlinebookstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Book.
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}