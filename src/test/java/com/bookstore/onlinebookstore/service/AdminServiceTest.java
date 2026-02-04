package com.bookstore.onlinebookstore.service;

import com.bookstore.onlinebookstore.dto.CreateBookRequest;
import com.bookstore.onlinebookstore.dto.CreateUserRequest;
import com.bookstore.onlinebookstore.entity.Book;
import com.bookstore.onlinebookstore.entity.Category;
import com.bookstore.onlinebookstore.entity.User;
import com.bookstore.onlinebookstore.repository.BookRepository;
import com.bookstore.onlinebookstore.repository.CategoryRepository;
import com.bookstore.onlinebookstore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AdminService
 * Tests admin operations including user creation and book management
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AdminService Unit Tests")
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminService adminService;

    private CreateUserRequest validUserRequest;
    private CreateBookRequest validBookRequest;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        // Setup valid user request
        validUserRequest = new CreateUserRequest();
        validUserRequest.setUsername("newuser");
        validUserRequest.setEmail("newuser@example.com");
        validUserRequest.setPassword("password123");
        validUserRequest.setRole("USER");

        // Setup test category
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Fiction");

        // Setup valid book request
        validBookRequest = new CreateBookRequest();
        validBookRequest.setTitle("Clean Code");
        validBookRequest.setAuthor("Robert C. Martin");
        validBookRequest.setPrice(45.99);
        validBookRequest.setCategoryId(1L);
        validBookRequest.setDescription("A handbook of agile software craftsmanship");
        validBookRequest.setImageUrl("https://example.com/cleancode.jpg");
    }

    // ==================== Create User Tests ====================

    @Test
    @DisplayName("Should create user successfully with valid data")
    void shouldCreateUserSuccessfully() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        adminService.createUser(validUserRequest);

        // Then
        verify(userRepository, times(1)).existsByUsername("newuser");
        verify(userRepository, times(1)).existsByEmail("newuser@example.com");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should save user with encoded password")
    void shouldSaveUserWithEncodedPassword() {
        // Given
        String encodedPassword = "encodedPassword123";
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        adminService.createUser(validUserRequest);

        // Then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getPassword()).isEqualTo(encodedPassword);
        assertThat(savedUser.getUsername()).isEqualTo("newuser");
        assertThat(savedUser.getEmail()).isEqualTo("newuser@example.com");
    }

    @Test
    @DisplayName("Should assign correct role from request")
    void shouldAssignCorrectRole() {
        // Given
        validUserRequest.setRole("ADMIN");
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        adminService.createUser(validUserRequest);

        // Then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getRole()).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("Should default to USER role when role is null")
    void shouldDefaultToUserRoleWhenNull() {
        // Given
        validUserRequest.setRole(null);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        adminService.createUser(validUserRequest);

        // Then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getRole()).isEqualTo("USER");
    }

    @Test
    @DisplayName("Should throw exception when username already exists")
    void shouldThrowExceptionWhenUsernameExists() {
        // Given
        when(userRepository.existsByUsername("newuser")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> adminService.createUser(validUserRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Username already exists");

        verify(userRepository, times(1)).existsByUsername("newuser");
        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void shouldThrowExceptionWhenEmailExists() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail("newuser@example.com")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> adminService.createUser(validUserRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Email already exists");

        verify(userRepository, times(1)).existsByUsername("newuser");
        verify(userRepository, times(1)).existsByEmail("newuser@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should validate username before email")
    void shouldValidateUsernameBeforeEmail() {
        // Given
        when(userRepository.existsByUsername("newuser")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> adminService.createUser(validUserRequest))
                .isInstanceOf(RuntimeException.class);

        verify(userRepository, times(1)).existsByUsername("newuser");
        verify(userRepository, never()).existsByEmail(anyString());
    }

    @Test
    @DisplayName("Should not encode password when username exists")
    void shouldNotEncodePasswordWhenUsernameExists() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> adminService.createUser(validUserRequest))
                .isInstanceOf(RuntimeException.class);

        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    @DisplayName("Should not encode password when email exists")
    void shouldNotEncodePasswordWhenEmailExists() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> adminService.createUser(validUserRequest))
                .isInstanceOf(RuntimeException.class);

        verify(passwordEncoder, never()).encode(anyString());
    }

    // ==================== Create Book Tests ====================

    @Test
    @DisplayName("Should create book successfully with valid data")
    void shouldCreateBookSuccessfully() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> {
            Book book = i.getArgument(0);
            book.setId(1L);
            return book;
        });

        // When
        Book result = adminService.createBook(validBookRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Clean Code");
        assertThat(result.getAuthor()).isEqualTo("Robert C. Martin");
        assertThat(result.getPrice()).isEqualTo(45.99);
        assertThat(result.getCategory()).isEqualTo(testCategory);
        assertThat(result.getDescription()).isEqualTo("A handbook of agile software craftsmanship");
        assertThat(result.getImageUrl()).isEqualTo("https://example.com/cleancode.jpg");

        verify(categoryRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("Should save book with all provided fields")
    void shouldSaveBookWithAllFields() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        adminService.createBook(validBookRequest);

        // Then
        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookCaptor.capture());

        Book savedBook = bookCaptor.getValue();
        assertThat(savedBook.getTitle()).isEqualTo("Clean Code");
        assertThat(savedBook.getAuthor()).isEqualTo("Robert C. Martin");
        assertThat(savedBook.getPrice()).isEqualTo(45.99);
        assertThat(savedBook.getCategory()).isEqualTo(testCategory);
        assertThat(savedBook.getDescription()).isEqualTo("A handbook of agile software craftsmanship");
        assertThat(savedBook.getImageUrl()).isEqualTo("https://example.com/cleancode.jpg");
    }

    @Test
    @DisplayName("Should throw exception when category not found")
    void shouldThrowExceptionWhenCategoryNotFound() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> adminService.createBook(validBookRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Category not found");

        verify(categoryRepository, times(1)).findById(1L);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    @DisplayName("Should handle null description")
    void shouldHandleNullDescription() {
        // Given
        validBookRequest.setDescription(null);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        Book result = adminService.createBook(validBookRequest);

        // Then
        assertThat(result.getDescription()).isNull();
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("Should handle null image URL")
    void shouldHandleNullImageUrl() {
        // Given
        validBookRequest.setImageUrl(null);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        Book result = adminService.createBook(validBookRequest);

        // Then
        assertThat(result.getImageUrl()).isNull();
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("Should handle empty description")
    void shouldHandleEmptyDescription() {
        // Given
        validBookRequest.setDescription("");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        Book result = adminService.createBook(validBookRequest);

        // Then
        assertThat(result.getDescription()).isEmpty();
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("Should handle zero price")
    void shouldHandleZeroPrice() {
        // Given
        validBookRequest.setPrice(0.0);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        Book result = adminService.createBook(validBookRequest);

        // Then
        assertThat(result.getPrice()).isEqualTo(0.0);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("Should handle high price values")
    void shouldHandleHighPrice() {
        // Given
        validBookRequest.setPrice(999.99);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        Book result = adminService.createBook(validBookRequest);

        // Then
        assertThat(result.getPrice()).isEqualTo(999.99);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("Should associate book with correct category")
    void shouldAssociateBookWithCategory() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        Book result = adminService.createBook(validBookRequest);

        // Then
        assertThat(result.getCategory()).isEqualTo(testCategory);
        assertThat(result.getCategory().getId()).isEqualTo(1L);
        assertThat(result.getCategory().getName()).isEqualTo("Fiction");
    }

    @Test
    @DisplayName("Should return saved book with all fields")
    void shouldReturnSavedBook() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> {
            Book book = i.getArgument(0);
            book.setId(99L);
            return book;
        });

        // When
        Book result = adminService.createBook(validBookRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(99L);
        assertThat(result.getTitle()).isEqualTo("Clean Code");
    }

    @Test
    @DisplayName("Should handle special characters in title")
    void shouldHandleSpecialCharactersInTitle() {
        // Given
        validBookRequest.setTitle("C++ & Java: A Guide!");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        Book result = adminService.createBook(validBookRequest);

        // Then
        assertThat(result.getTitle()).isEqualTo("C++ & Java: A Guide!");
    }

    @Test
    @DisplayName("Should handle special characters in author name")
    void shouldHandleSpecialCharactersInAuthor() {
        // Given
        validBookRequest.setAuthor("O'Reilly & Associates");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        Book result = adminService.createBook(validBookRequest);

        // Then
        assertThat(result.getAuthor()).isEqualTo("O'Reilly & Associates");
    }

    // ==================== Validation Order Tests ====================

    @Test
    @DisplayName("Should validate in correct order for user creation")
    void shouldValidateUserInCorrectOrder() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        adminService.createUser(validUserRequest);

        // Then
        var inOrder = inOrder(userRepository, passwordEncoder);
        inOrder.verify(userRepository).existsByUsername("newuser");
        inOrder.verify(userRepository).existsByEmail("newuser@example.com");
        inOrder.verify(passwordEncoder).encode("password123");
        inOrder.verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should fetch category before saving book")
    void shouldFetchCategoryBeforeSavingBook() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        adminService.createBook(validBookRequest);

        // Then
        var inOrder = inOrder(categoryRepository, bookRepository);
        inOrder.verify(categoryRepository).findById(1L);
        inOrder.verify(bookRepository).save(any(Book.class));
    }

    @Test
    @DisplayName("Should handle repository save failure for user")
    void shouldHandleUserSaveFailure() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class)))
                .thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThatThrownBy(() -> adminService.createUser(validUserRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database error");
    }

    @Test
    @DisplayName("Should handle repository save failure for book")
    void shouldHandleBookSaveFailure() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(bookRepository.save(any(Book.class)))
                .thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThatThrownBy(() -> adminService.createBook(validBookRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database error");
    }
}