package com.bookstore.onlinebookstore.dto;

import lombok.Data;

@Data
public class CreateBookRequest {
    private String title;
    private String author;
    private Double price;
    private Long categoryId;
    private String description;
    private String imageUrl;
}