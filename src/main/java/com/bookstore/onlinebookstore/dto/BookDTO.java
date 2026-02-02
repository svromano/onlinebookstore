package com.bookstore.onlinebookstore.dto;

import lombok.Data;


/**
 * The type Book dto.
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
     * Instantiates a new Book dto.
     *
     * @param id           the id
     * @param title        the title
     * @param author       the author
     * @param price        the price
     * @param description  the description
     * @param imageUrl     the image url
     * @param categoryName the category name
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