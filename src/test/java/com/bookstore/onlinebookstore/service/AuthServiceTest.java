package com.bookstore.onlinebookstore.service;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AuthService
 * Tests authentication-related operations with mocked dependencies
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Unit Tests")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("plainPassword123");
    }

    @Test
    @DisplayName("Should register user with encoded password")
    void shouldRegisterUserWithEncodedPassword() {
        // Given
        String encodedPassword = "encodedPassword123";
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        authService.register(testUser);

        // Then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getPassword()).isEqualTo(encodedPassword);
        assertThat(savedUser.getPassword()).isNotEqualTo("plainPassword123");
    }

    @Test
    @DisplayName("Should call password encoder with original password")
    void shouldCallPasswordEncoderWithOriginalPassword() {
        // Given
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        authService.register(testUser);

        // Then
        verify(passwordEncoder, times(1)).encode("plainPassword123");
    }

    @Test
    @DisplayName("Should save user to repository")
    void shouldSaveUserToRepository() {
        // Given
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        authService.register(testUser);

        // Then
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Should preserve user fields except password")
    void shouldPreserveUserFieldsExceptPassword() {
        // Given
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        authService.register(testUser);

        // Then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getUsername()).isEqualTo("testuser");
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Should handle null password")
    void shouldHandleNullPassword() {
        // Given
        testUser.setPassword(null);
        when(passwordEncoder.encode(null)).thenReturn("encodedNull");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        authService.register(testUser);

        // Then
        verify(passwordEncoder, times(1)).encode(null);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should handle empty password")
    void shouldHandleEmptyPassword() {
        // Given
        testUser.setPassword("");
        when(passwordEncoder.encode("")).thenReturn("encodedEmpty");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        authService.register(testUser);

        // Then
        verify(passwordEncoder, times(1)).encode("");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should handle special characters in password")
    void shouldHandleSpecialCharactersInPassword() {
        // Given
        String specialPassword = "P@ssw0rd!#$%^&*()";
        testUser.setPassword(specialPassword);
        when(passwordEncoder.encode(specialPassword)).thenReturn("encodedSpecial");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        authService.register(testUser);

        // Then
        verify(passwordEncoder, times(1)).encode(specialPassword);
    }
}