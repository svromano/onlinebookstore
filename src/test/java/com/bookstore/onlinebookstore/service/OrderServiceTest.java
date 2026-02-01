package com.bookstore.onlinebookstore.service;

import com.bookstore.onlinebookstore.dto.OrderItemRequest;
import com.bookstore.onlinebookstore.entity.Order;
import com.bookstore.onlinebookstore.entity.OrderItem;
import com.bookstore.onlinebookstore.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for OrderService
 * Tests the business logic in isolation using Mockito to mock dependencies
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService Unit Tests")
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private List<OrderItemRequest> orderItems;

    @BeforeEach
    void setUp() {
        // Prepare test data
        OrderItemRequest item1 = new OrderItemRequest();
        item1.setBookId(1L);
        item1.setTitle("Clean Code");
        item1.setPrice(45.99);
        item1.setQuantity(2);

        OrderItemRequest item2 = new OrderItemRequest();
        item2.setBookId(2L);
        item2.setTitle("Effective Java");
        item2.setPrice(50.00);
        item2.setQuantity(1);

        orderItems = Arrays.asList(item1, item2);
    }

    @Test
    @DisplayName("Should create order with correct total calculation")
    void shouldCreateOrderWithCorrectTotal() {
        // Given
        Long userId = 1L;
        String username = "testuser";

        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        orderService.createOrder(userId, username, orderItems);

        // Then
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository, times(1)).save(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();
        assertThat(savedOrder.getUserId()).isEqualTo(userId);
        assertThat(savedOrder.getUsername()).isEqualTo(username);
        assertThat(savedOrder.getTotal()).isCloseTo(141.98, within(0.01)); // (45.99 * 2) + (50.00 * 1)
        assertThat(savedOrder.getItems()).hasSize(2);
    }

    @Test
    @DisplayName("Should create order items with correct values")
    void shouldCreateOrderItemsWithCorrectValues() {
        // Given
        Long userId = 1L;
        String username = "testuser";

        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        orderService.createOrder(userId, username, orderItems);

        // Then
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();
        List<OrderItem> items = savedOrder.getItems();

        assertThat(items).hasSize(2);

        OrderItem firstItem = items.get(0);
        assertThat(firstItem.getBookId()).isEqualTo(1L);
        assertThat(firstItem.getTitle()).isEqualTo("Clean Code");
        assertThat(firstItem.getPrice()).isEqualTo(45.99);
        assertThat(firstItem.getQuantity()).isEqualTo(2);
        assertThat(firstItem.getOrder()).isEqualTo(savedOrder);
    }

    @Test
    @DisplayName("Should use default username when username is null")
    void shouldUseDefaultUsernameWhenNull() {
        // Given
        Long userId = 1L;
        String username = null;

        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        orderService.createOrder(userId, username, orderItems);

        // Then
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();
        assertThat(savedOrder.getUsername()).isEqualTo("guest");
    }

    @Test
    @DisplayName("Should handle empty items list")
    void shouldHandleEmptyItemsList() {
        // Given
        Long userId = 1L;
        String username = "testuser";
        List<OrderItemRequest> emptyItems = Arrays.asList();

        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        orderService.createOrder(userId, username, emptyItems);

        // Then
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();
        assertThat(savedOrder.getTotal()).isEqualTo(0.0);
        assertThat(savedOrder.getItems()).isEmpty();
    }

    @Test
    @DisplayName("Should calculate total correctly with multiple quantities")
    void shouldCalculateTotalCorrectlyWithMultipleQuantities() {
        // Given
        OrderItemRequest item = new OrderItemRequest();
        item.setBookId(1L);
        item.setTitle("Test Book");
        item.setPrice(10.50);
        item.setQuantity(5);

        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        orderService.createOrder(1L, "user", Arrays.asList(item));

        // Then
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();
        assertThat(savedOrder.getTotal()).isCloseTo(52.50, within(0.01)); // 10.50 * 5
    }
}