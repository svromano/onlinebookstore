package com.bookstore.onlinebookstore.service;

import com.bookstore.onlinebookstore.entity.User;
import com.bookstore.onlinebookstore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CustomUserDetailsService
 * Tests Spring Security UserDetailsService implementation
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CustomUserDetailsService Unit Tests")
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword123");
    }

    @Test
    @DisplayName("Should load user by username successfully")
    void shouldLoadUserByUsernameSuccessfully() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        // Then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("testuser");
        assertThat(userDetails.getPassword()).isEqualTo("encodedPassword123");
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    @DisplayName("Should assign USER role to loaded user")
    void shouldAssignUserRole() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        // Then
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_USER");
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("nonexistent"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository, times(1)).findByUsername("nonexistent");
    }

    @Test
    @DisplayName("Should handle null username")
    void shouldHandleNullUsername() {
        // Given
        when(userRepository.findByUsername(null)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(null))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository, times(1)).findByUsername(null);
    }

    @Test
    @DisplayName("Should handle empty username")
    void shouldHandleEmptyUsername() {
        // Given
        when(userRepository.findByUsername("")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(""))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository, times(1)).findByUsername("");
    }

    @Test
    @DisplayName("Should create UserDetails with correct password")
    void shouldCreateUserDetailsWithCorrectPassword() {
        // Given
        String encodedPassword = "superSecureEncodedPassword";
        testUser.setPassword(encodedPassword);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        // Then
        assertThat(userDetails.getPassword()).isEqualTo(encodedPassword);
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    @DisplayName("Should handle username with special characters")
    void shouldHandleUsernameWithSpecialCharacters() {
        // Given
        String specialUsername = "user@example.com";
        testUser.setUsername(specialUsername);
        when(userRepository.findByUsername(specialUsername)).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername(specialUsername);

        // Then
        assertThat(userDetails.getUsername()).isEqualTo(specialUsername);
        verify(userRepository, times(1)).findByUsername(specialUsername);
    }

    @Test
    @DisplayName("Should handle case-sensitive username")
    void shouldHandleCaseSensitiveUsername() {
        // Given
        when(userRepository.findByUsername("TestUser")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("TestUser"))
                .isInstanceOf(UsernameNotFoundException.class);

        verify(userRepository, times(1)).findByUsername("TestUser");
    }

    @Test
    @DisplayName("Should return enabled UserDetails")
    void shouldReturnEnabledUserDetails() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        // Then
        assertThat(userDetails.isEnabled()).isTrue();
        assertThat(userDetails.isAccountNonExpired()).isTrue();
        assertThat(userDetails.isAccountNonLocked()).isTrue();
        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
    }

    @Test
    @DisplayName("Should call repository only once per request")
    void shouldCallRepositoryOnlyOnce() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When
        userDetailsService.loadUserByUsername("testuser");

        // Then
        verify(userRepository, times(1)).findByUsername("testuser");
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Should preserve user entity data in UserDetails")
    void shouldPreserveUserEntityData() {
        // Given
        testUser.setUsername("john.doe");
        testUser.setPassword("hashedPassword456");
        when(userRepository.findByUsername("john.doe")).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("john.doe");

        // Then
        assertThat(userDetails.getUsername()).isEqualTo("john.doe");
        assertThat(userDetails.getPassword()).isEqualTo("hashedPassword456");
    }

    @Test
    @DisplayName("Should throw exception with correct message")
    void shouldThrowExceptionWithCorrectMessage() {
        // Given
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("anyuser"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found")
                .hasNoCause();
    }
}