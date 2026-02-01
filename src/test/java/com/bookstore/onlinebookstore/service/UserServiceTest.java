package com.bookstore.onlinebookstore.service;

import com.bookstore.onlinebookstore.dto.RegisterRequest;
import com.bookstore.onlinebookstore.entity.User;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserService
 * Tests user registration with validation and password encoding
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private RegisterRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new RegisterRequest("testuser", "test@example.com", "password123");
    }

    // ==================== Successful Registration Tests ====================

    @Test
    @DisplayName("Should register user successfully with valid data")
    void shouldRegisterUserSuccessfully() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        userService.register(validRequest);

        // Then
        verify(userRepository, times(1)).existsByUsername("testuser");
        verify(userRepository, times(1)).existsByEmail("test@example.com");
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
        userService.register(validRequest);

        // Then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getPassword()).isEqualTo(encodedPassword);
    }

    @Test
    @DisplayName("Should save user with correct username and email")
    void shouldSaveUserWithCorrectData() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        userService.register(validRequest);

        // Then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getUsername()).isEqualTo("testuser");
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
    }

    // ==================== Duplicate Username Tests ====================

    @Test
    @DisplayName("Should throw exception when username already exists")
    void shouldThrowExceptionWhenUsernameExists() {
        // Given
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.register(validRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Username already exists");

        verify(userRepository, times(1)).existsByUsername("testuser");
        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should check username existence before email")
    void shouldCheckUsernameBeforeEmail() {
        // Given
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.register(validRequest))
                .isInstanceOf(RuntimeException.class);

        verify(userRepository, times(1)).existsByUsername("testuser");
        verify(userRepository, never()).existsByEmail(anyString());
    }

    // ==================== Duplicate Email Tests ====================

    @Test
    @DisplayName("Should throw exception when email already exists")
    void shouldThrowExceptionWhenEmailExists() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.register(validRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Email already exists");

        verify(userRepository, times(1)).existsByUsername("testuser");
        verify(userRepository, times(1)).existsByEmail("test@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should not encode password when email exists")
    void shouldNotEncodePasswordWhenEmailExists() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.register(validRequest))
                .isInstanceOf(RuntimeException.class);

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    // ==================== Password Encoding Tests ====================

    @Test
    @DisplayName("Should encode password before saving")
    void shouldEncodePasswordBeforeSaving() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded123");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        userService.register(validRequest);

        // Then
        verify(passwordEncoder, times(1)).encode("password123");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getPassword()).isEqualTo("encoded123");
    }

    @Test
    @DisplayName("Should handle special characters in password")
    void shouldHandleSpecialCharactersInPassword() {
        // Given
        RegisterRequest specialRequest = new RegisterRequest(
                "user",
                "user@test.com",
                "P@ssw0rd!#$%"
        );
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode("P@ssw0rd!#$%")).thenReturn("encodedSpecial");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        userService.register(specialRequest);

        // Then
        verify(passwordEncoder, times(1)).encode("P@ssw0rd!#$%");
    }

    // ==================== Edge Cases Tests ====================

    @Test
    @DisplayName("Should handle email with special format")
    void shouldHandleEmailWithSpecialFormat() {
        // Given
        RegisterRequest emailRequest = new RegisterRequest(
                "user",
                "user+test@example.co.uk",
                "password"
        );
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        userService.register(emailRequest);

        // Then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getEmail()).isEqualTo("user+test@example.co.uk");
    }

    @Test
    @DisplayName("Should handle username with underscores and numbers")
    void shouldHandleUsernameWithSpecialChars() {
        // Given
        RegisterRequest specialUsername = new RegisterRequest(
                "user_123",
                "test@example.com",
                "password"
        );
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        userService.register(specialUsername);

        // Then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getUsername()).isEqualTo("user_123");
    }

    @Test
    @DisplayName("Should create new User entity for each registration")
    void shouldCreateNewUserEntity() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        userService.register(validRequest);

        // Then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNull(); // New entity should not have ID yet
    }

    // ==================== Validation Order Tests ====================

    @Test
    @DisplayName("Should validate in correct order: username, email, then save")
    void shouldValidateInCorrectOrder() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        userService.register(validRequest);

        // Then
        var inOrder = inOrder(userRepository, passwordEncoder);
        inOrder.verify(userRepository).existsByUsername("testuser");
        inOrder.verify(userRepository).existsByEmail("test@example.com");
        inOrder.verify(passwordEncoder).encode("password123");
        inOrder.verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should not save when validation fails")
    void shouldNotSaveWhenValidationFails() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.register(validRequest))
                .isInstanceOf(RuntimeException.class);

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should handle repository save failure gracefully")
    void shouldHandleRepositorySaveFailure() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class)))
                .thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThatThrownBy(() -> userService.register(validRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database error");
    }
}