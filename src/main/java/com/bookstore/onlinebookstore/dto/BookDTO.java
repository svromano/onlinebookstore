package com.bookstore.onlinebookstore.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for Book information.
 * Using a DTO allows us to "flatten" the data—for example, including the category name
 * directly instead of the entire Category object.
 * * @Data: A Lombok annotation that generates Getters, Setters, toString,
 * equals, and hashCode methods automatically.
 */
@Data
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private double price;
    private String description;
    private String imageUrl;

    // Instead of sending the full Category object, we just send the name.
    // This makes the JSON response cleaner and easier for the frontend to display.
    private String categoryName;

    /**
     * All-args constructor.
     * This is useful when using JPQL queries (like "SELECT new BookDTO(...)")
     * to map database results directly into this object.
     */
    public BookDTO(Long id, String title, String author, double price, String description, String imageUrl, String categoryName) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.categoryName = categoryName;
    }
}