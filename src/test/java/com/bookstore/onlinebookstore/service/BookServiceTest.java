package com.bookstore.onlinebookstore.service;

import com.bookstore.onlinebookstore.entity.Book;
import com.bookstore.onlinebookstore.entity.Category;
import com.bookstore.onlinebookstore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BookService
 * Tests book-related operations with mocked repository
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

    // ==================== getAllBooks Tests ====================

    @Test
    @DisplayName("Should return all books")
    void shouldReturnAllBooks() {
        // Given
        when(bookRepository.findAll()).thenReturn(testBooks);

        // When
        List<Book> result = bookService.getAllBooks();

        // Then
        assertThat(result).hasSize(3);
        assertThat(result).isEqualTo(testBooks);
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no books exist")
    void shouldReturnEmptyListWhenNoBooksExist() {
        // Given
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Book> result = bookService.getAllBooks();

        // Then
        assertThat(result).isEmpty();
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should call repository findAll method")
    void shouldCallRepositoryFindAll() {
        // Given
        when(bookRepository.findAll()).thenReturn(testBooks);

        // When
        bookService.getAllBooks();

        // Then
        verify(bookRepository, times(1)).findAll();
        verifyNoMoreInteractions(bookRepository);
    }

    // ==================== getBooksByCategory Tests ====================

    @Test
    @DisplayName("Should return books by category ID")
    void shouldReturnBooksByCategoryId() {
        // Given
        Long categoryId = 1L;
        List<Book> fictionBooks = Arrays.asList(testBooks.get(0), testBooks.get(1));
        when(bookRepository.findByCategoryId(categoryId)).thenReturn(fictionBooks);

        // When
        List<Book> result = bookService.getBooksByCategory(categoryId);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(book -> book.getCategory().getId().equals(categoryId));
        verify(bookRepository, times(1)).findByCategoryId(categoryId);
    }

    @Test
    @DisplayName("Should return empty list when category has no books")
    void shouldReturnEmptyListWhenCategoryHasNoBooks() {
        // Given
        Long categoryId = 999L;
        when(bookRepository.findByCategoryId(categoryId)).thenReturn(Collections.emptyList());

        // When
        List<Book> result = bookService.getBooksByCategory(categoryId);

        // Then
        assertThat(result).isEmpty();
        verify(bookRepository, times(1)).findByCategoryId(categoryId);
    }

    @Test
    @DisplayName("Should handle null category ID")
    void shouldHandleNullCategoryId() {
        // Given
        when(bookRepository.findByCategoryId(null)).thenReturn(Collections.emptyList());

        // When
        List<Book> result = bookService.getBooksByCategory(null);

        // Then
        assertThat(result).isEmpty();
        verify(bookRepository, times(1)).findByCategoryId(null);
    }

    @Test
    @DisplayName("Should return single book for category with one book")
    void shouldReturnSingleBookForCategoryWithOneBook() {
        // Given
        Long categoryId = 2L;
        List<Book> sciFiBooks = Arrays.asList(testBooks.get(2));
        when(bookRepository.findByCategoryId(categoryId)).thenReturn(sciFiBooks);

        // When
        List<Book> result = bookService.getBooksByCategory(categoryId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Dune");
        verify(bookRepository, times(1)).findByCategoryId(categoryId);
    }

    // ==================== searchBooks Tests ====================

    @Test
    @DisplayName("Should search books by title")
    void shouldSearchBooksByTitle() {
        // Given
        String keyword = "Clean";
        List<Book> searchResult = Arrays.asList(testBooks.get(0));
        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword))
                .thenReturn(searchResult);

        // When
        List<Book> result = bookService.searchBooks(keyword);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).contains("Clean");
        verify(bookRepository, times(1))
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }

    @Test
    @DisplayName("Should search books by author")
    void shouldSearchBooksByAuthor() {
        // Given
        String keyword = "Bloch";
        List<Book> searchResult = Arrays.asList(testBooks.get(1));
        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword))
                .thenReturn(searchResult);

        // When
        List<Book> result = bookService.searchBooks(keyword);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAuthor()).contains("Bloch");
        verify(bookRepository, times(1))
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }

    @Test
    @DisplayName("Should return empty list when no books match search")
    void shouldReturnEmptyListWhenNoMatch() {
        // Given
        String keyword = "NonExistent";
        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword))
                .thenReturn(Collections.emptyList());

        // When
        List<Book> result = bookService.searchBooks(keyword);

        // Then
        assertThat(result).isEmpty();
        verify(bookRepository, times(1))
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }

    @Test
    @DisplayName("Should handle empty search keyword")
    void shouldHandleEmptySearchKeyword() {
        // Given
        String keyword = "";
        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword))
                .thenReturn(testBooks);

        // When
        List<Book> result = bookService.searchBooks(keyword);

        // Then
        assertThat(result).hasSize(3);
        verify(bookRepository, times(1))
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }

    @Test
    @DisplayName("Should handle null search keyword")
    void shouldHandleNullSearchKeyword() {
        // Given
        String keyword = null;
        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword))
                .thenReturn(Collections.emptyList());

        // When
        List<Book> result = bookService.searchBooks(keyword);

        // Then
        verify(bookRepository, times(1))
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }

    @Test
    @DisplayName("Should search case-insensitively")
    void shouldSearchCaseInsensitively() {
        // Given
        String keyword = "clean";
        List<Book> searchResult = Arrays.asList(testBooks.get(0));
        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword))
                .thenReturn(searchResult);

        // When
        List<Book> result = bookService.searchBooks(keyword);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Clean Code");
        verify(bookRepository, times(1))
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }

    @Test
    @DisplayName("Should return multiple books matching search")
    void shouldReturnMultipleBooksMatchingSearch() {
        // Given
        String keyword = "Java";
        List<Book> searchResult = Arrays.asList(testBooks.get(1));
        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword))
                .thenReturn(searchResult);

        // When
        List<Book> result = bookService.searchBooks(keyword);

        // Then
        assertThat(result).isNotEmpty();
        verify(bookRepository, times(1))
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }

    @Test
    @DisplayName("Should handle special characters in search")
    void shouldHandleSpecialCharactersInSearch() {
        // Given
        String keyword = "C++ & Java";
        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword))
                .thenReturn(Collections.emptyList());

        // When
        List<Book> result = bookService.searchBooks(keyword);

        // Then
        verify(bookRepository, times(1))
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }

    @Test
    @DisplayName("Should pass same keyword for both title and author search")
    void shouldPassSameKeywordForBothSearches() {
        // Given
        String keyword = "Test";
        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword))
                .thenReturn(Collections.emptyList());

        // When
        bookService.searchBooks(keyword);

        // Then
        verify(bookRepository, times(1))
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                        eq(keyword),
                        eq(keyword)
                );
    }
}