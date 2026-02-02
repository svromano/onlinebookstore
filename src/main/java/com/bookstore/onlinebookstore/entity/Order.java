package com.bookstore.onlinebookstore.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a "Sales Receipt" in your system.
 * It tracks who bought what, how much it cost, and when the transaction happened.
 * 1. PERSISTENCE: Maps to the 'orders' table to store historical transaction data.
 * 2. IDENTITY: Stores the 'userId' and 'username' to link the purchase to a specific customer.
 * 3. RELATIONSHIP: Uses a One-To-Many relationship with 'OrderItem'. This means one single
 * order can contain multiple different books (items).
 * 4. CASCADE: 'CascadeType.ALL' ensures that when we save an Order, all the individual items
 * inside it are saved automatically, keeping the data synchronized.
 */
@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "username")
    private String username;

    private double total;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JsonManagedReference
    private List<OrderItem> items = new ArrayList<>();
}