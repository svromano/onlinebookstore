package com.bookstore.onlinebookstore.dto;

import lombok.Data;

@Data
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private double price;
    private String description;
    private String imageUrl;
    private String categoryName;

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
