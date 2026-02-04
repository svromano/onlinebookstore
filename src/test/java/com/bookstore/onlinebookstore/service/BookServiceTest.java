package com.bookstore.onlinebookstore.service;

import com.bookstore.onlinebookstore.entity.Book;
import com.bookstore.onlinebookstore.entity.Category;
import com.bookstore.onlinebookstore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BookService
 * Tests book-related operations with pagination and mocked repository
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BookService Unit Tests")
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private List<Book> testBooks;
    private Category fiction;
    private Category scienceFiction;

    @BeforeEach
    void setUp() {
        // Create categories
        fiction = new Category();
        fiction.setId(1L);
        fiction.setName("Fiction");

        scienceFiction = new Category();
        scienceFiction.setId(2L);
        scienceFiction.setName("Science Fiction");

        // Create test books
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Clean Code");
        book1.setAuthor("Robert Martin");
        book1.setPrice(45.99);
        book1.setCategory(fiction);

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Effective Java");
        book2.setAuthor("Joshua Bloch");
        book2.setPrice(50.00);
        book2.setCategory(fiction);

        Book book3 = new Book();
        book3.setId(3L);
        book3.setTitle("Dune");
        book3.setAuthor("Frank Herbert");
        book3.setPrice(25.99);
        book3.setCategory(scienceFiction);

        testBooks = Arrays.asList(book1, book2, book3);
    }

    // ==================== Get All Books Tests ====================

    @Test
    @DisplayName("Should return all books with pagination")
    void shouldReturnAllBooksWithPagination() {
        // Given
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> expectedPage = new PageImpl<>(testBooks, pageable, testBooks.size());

        when(bookRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        // When
        Page<Book> result = bookService.getBooks(null, null, page, size);

        // Then
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getContent()).isEqualTo(testBooks);
        verify(bookRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Should return empty page when no books exist")
    void shouldReturnEmptyPageWhenNoBooksExist() {
        // Given
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(bookRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        // When
        Page<Book> result = bookService.getBooks(null, null, page, size);

        // Then
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0);
        verify(bookRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Should handle different page sizes")
    void shouldHandleDifferentPageSizes() {
        // Given
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> expectedPage = new PageImpl<>(testBooks, pageable, testBooks.size());

        when(bookRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        // When
        Page<Book> result = bookService.getBooks(null, null, page, size);

        // Then
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(bookRepository).findAll(pageableCaptor.capture());

        Pageable capturedPageable = pageableCaptor.getValue();
        assertThat(capturedPageable.getPageSize()).isEqualTo(10);
        assertThat(capturedPageable.getPageNumber()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should handle different page numbers")
    void shouldHandleDifferentPageNumbers() {
        // Given
        int page = 2;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> expectedPage = new PageImpl<>(Collections.emptyList(), pageable, 15);

        when(bookRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        // When
        bookService.getBooks(null, null, page, size);

        // Then
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(bookRepository).findAll(pageableCaptor.capture());

        Pageable capturedPageable = pageableCaptor.getValue();
        assertThat(capturedPageable.getPageNumber()).isEqualTo(2);
    }

    // ==================== Get Books by Category Tests ====================

    @Test
    @DisplayName("Should return books by category ID with pagination")
    void shouldReturnBooksByCategoryId() {
        // Given
        Long categoryId = 1L;
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        List<Book> fictionBooks = Arrays.asList(testBooks.get(0), testBooks.get(1));
        Page<Book> expectedPage = new PageImpl<>(fictionBooks, pageable, fictionBooks.size());

        when(bookRepository.findByCategoryId(eq(categoryId), any(Pageable.class)))
                .thenReturn(expectedPage);

        // When
        Page<Book> result = bookService.getBooks(null, categoryId, page, size);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).allMatch(book -> book.getCategory().getId().equals(categoryId));
        verify(bookRepository, times(1)).findByCategoryId(categoryId, pageable);
    }

    @Test
    @DisplayName("Should return empty page when category has no books")
    void shouldReturnEmptyPageWhenCategoryHasNoBooks() {
        // Given
        Long categoryId = 999L;
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(bookRepository.findByCategoryId(eq(categoryId), any(Pageable.class)))
                .thenReturn(emptyPage);

        // When
        Page<Book> result = bookService.getBooks(null, categoryId, page, size);

        // Then
        assertThat(result.getContent()).isEmpty();
        verify(bookRepository, times(1)).findByCategoryId(categoryId, pageable);
    }

    // ==================== Search Books Tests ====================

    @Test
    @DisplayName("Should search books by query with pagination")
    void shouldSearchBooksByQuery() {
        // Given
        String query = "Clean";
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        List<Book> searchResult = Arrays.asList(testBooks.get(0));
        Page<Book> expectedPage = new PageImpl<>(searchResult, pageable, searchResult.size());

        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                eq(query), eq(query), any(Pageable.class)))
                .thenReturn(expectedPage);

        // When
        Page<Book> result = bookService.getBooks(query, null, page, size);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).contains("Clean");
        verify(bookRepository, times(1))
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query, pageable);
    }

    @Test
    @DisplayName("Should search books by author with pagination")
    void shouldSearchBooksByAuthor() {
        // Given
        String query = "Bloch";
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        List<Book> searchResult = Arrays.asList(testBooks.get(1));
        Page<Book> expectedPage = new PageImpl<>(searchResult, pageable, searchResult.size());

        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                eq(query), eq(query), any(Pageable.class)))
                .thenReturn(expectedPage);

        // When
        Page<Book> result = bookService.getBooks(query, null, page, size);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getAuthor()).contains("Bloch");
        verify(bookRepository, times(1))
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query, pageable);
    }

    @Test
    @DisplayName("Should return empty page when no books match search")
    void shouldReturnEmptyPageWhenNoMatch() {
        // Given
        String query = "NonExistent";
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                eq(query), eq(query), any(Pageable.class)))
                .thenReturn(emptyPage);

        // When
        Page<Book> result = bookService.getBooks(query, null, page, size);

        // Then
        assertThat(result.getContent()).isEmpty();
        verify(bookRepository, times(1))
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query, pageable);
    }

    @Test
    @DisplayName("Should handle empty search query")
    void shouldHandleEmptySearchQuery() {
        // Given
        String query = "";
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> expectedPage = new PageImpl<>(testBooks, pageable, testBooks.size());

        when(bookRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        // When
        Page<Book> result = bookService.getBooks(query, null, page, size);

        // Then
        assertThat(result.getContent()).hasSize(3);
        verify(bookRepository, times(1)).findAll(pageable);
        verify(bookRepository, never())
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(anyString(), anyString(), any(Pageable.class));
    }

    // ==================== Search with Category Tests ====================

    @Test
    @DisplayName("Should search books by query and category")
    void shouldSearchBooksByQueryAndCategory() {
        // Given
        String query = "Code";
        Long categoryId = 1L;
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        List<Book> searchResult = Arrays.asList(testBooks.get(0));
        Page<Book> expectedPage = new PageImpl<>(searchResult, pageable, searchResult.size());

        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseAndCategoryId(
                eq(query), eq(query), eq(categoryId), any(Pageable.class)))
                .thenReturn(expectedPage);

        // When
        Page<Book> result = bookService.getBooks(query, categoryId, page, size);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).contains("Clean");
        assertThat(result.getContent().get(0).getCategory().getId()).isEqualTo(categoryId);
        verify(bookRepository, times(1))
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseAndCategoryId(
                        query, query, categoryId, pageable);
    }

    @Test
    @DisplayName("Should return empty page when query and category have no matches")
    void shouldReturnEmptyPageWhenQueryAndCategoryNoMatch() {
        // Given
        String query = "NonExistent";
        Long categoryId = 1L;
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseAndCategoryId(
                eq(query), eq(query), eq(categoryId), any(Pageable.class)))
                .thenReturn(emptyPage);

        // When
        Page<Book> result = bookService.getBooks(query, categoryId, page, size);

        // Then
        assertThat(result.getContent()).isEmpty();
        verify(bookRepository, times(1))
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseAndCategoryId(
                        query, query, categoryId, pageable);
    }

    @Test
    @DisplayName("Should handle empty query with category ID")
    void shouldHandleEmptyQueryWithCategory() {
        // Given
        String query = "";
        Long categoryId = 1L;
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        List<Book> fictionBooks = Arrays.asList(testBooks.get(0), testBooks.get(1));
        Page<Book> expectedPage = new PageImpl<>(fictionBooks, pageable, fictionBooks.size());

        when(bookRepository.findByCategoryId(eq(categoryId), any(Pageable.class)))
                .thenReturn(expectedPage);

        // When
        Page<Book> result = bookService.getBooks(query, categoryId, page, size);

        // Then
        assertThat(result.getContent()).hasSize(2);
        verify(bookRepository, times(1)).findByCategoryId(categoryId, pageable);
        verify(bookRepository, never())
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseAndCategoryId(
                        anyString(), anyString(), anyLong(), any(Pageable.class));
    }

    // ==================== Edge Cases and Special Scenarios ====================

    @Test
    @DisplayName("Should pass same query for both title and author search")
    void shouldPassSameQueryForBothSearches() {
        // Given
        String query = "Test";
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                anyString(), anyString(), any(Pageable.class)))
                .thenReturn(emptyPage);

        // When
        bookService.getBooks(query, null, page, size);

        // Then
        verify(bookRepository, times(1))
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                        eq(query),
                        eq(query),
                        eq(pageable)
                );
    }

    @Test
    @DisplayName("Should use correct repository method based on parameters")
    void shouldUseCorrectRepositoryMethod() {
        // Given
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(bookRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);
        when(bookRepository.findByCategoryId(anyLong(), any(Pageable.class))).thenReturn(emptyPage);
        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                anyString(), anyString(), any(Pageable.class))).thenReturn(emptyPage);
        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseAndCategoryId(
                anyString(), anyString(), anyLong(), any(Pageable.class))).thenReturn(emptyPage);

        // When & Then - No filters
        bookService.getBooks(null, null, page, size);
        verify(bookRepository, times(1)).findAll(any(Pageable.class));

        // When & Then - Only category
        bookService.getBooks(null, 1L, page, size);
        verify(bookRepository, times(1)).findByCategoryId(eq(1L), any(Pageable.class));

        // When & Then - Only query
        bookService.getBooks("test", null, page, size);
        verify(bookRepository, times(1))
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                        eq("test"), eq("test"), any(Pageable.class));

        // When & Then - Both query and category
        bookService.getBooks("test", 1L, page, size);
        verify(bookRepository, times(1))
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseAndCategoryId(
                        eq("test"), eq("test"), eq(1L), any(Pageable.class));
    }

    @Test
    @DisplayName("Should handle large page numbers")
    void shouldHandleLargePageNumbers() {
        // Given
        int page = 999;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(bookRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        // When
        Page<Book> result = bookService.getBooks(null, null, page, size);

        // Then
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(bookRepository).findAll(pageableCaptor.capture());
        assertThat(pageableCaptor.getValue().getPageNumber()).isEqualTo(999);
    }

    @Test
    @DisplayName("Should handle special characters in search query")
    void shouldHandleSpecialCharactersInSearch() {
        // Given
        String query = "C++ & Java!";
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                eq(query), eq(query), any(Pageable.class)))
                .thenReturn(emptyPage);

        // When
        bookService.getBooks(query, null, page, size);

        // Then
        verify(bookRepository, times(1))
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query, pageable);
    }

    @Test
    @DisplayName("Should create pageable with correct page and size")
    void shouldCreatePageableWithCorrectParameters() {
        // Given
        int page = 3;
        int size = 15;
        Page<Book> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), 0);

        when(bookRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        // When
        bookService.getBooks(null, null, page, size);

        // Then
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(bookRepository).findAll(pageableCaptor.capture());

        Pageable capturedPageable = pageableCaptor.getValue();
        assertThat(capturedPageable.getPageNumber()).isEqualTo(3);
        assertThat(capturedPageable.getPageSize()).isEqualTo(15);
    }

    @Test
    @DisplayName("Should return page metadata correctly")
    void shouldReturnPageMetadataCorrectly() {
        // Given
        int page = 1;
        int size = 2;
        int totalElements = 10;
        Pageable pageable = PageRequest.of(page, size);
        List<Book> pageContent = Arrays.asList(testBooks.get(0), testBooks.get(1));
        Page<Book> expectedPage = new PageImpl<>(pageContent, pageable, totalElements);

        when(bookRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        // When
        Page<Book> result = bookService.getBooks(null, null, page, size);

        // Then
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.getSize()).isEqualTo(2);
        assertThat(result.getTotalElements()).isEqualTo(10);
        assertThat(result.getTotalPages()).isEqualTo(5); // 10 elements / 2 per page
        assertThat(result.getContent()).hasSize(2);
    }
}