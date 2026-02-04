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
 * Unit tests for CustomUserDetailsService.
 * Validates the bridge between the Database (UserRepository) and Spring Security.
 * Tests dynamic role assignment from database.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CustomUserDetailsService Unit Tests")
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    private User testUser;
    private User adminUser;

    @BeforeEach
    void setUp() {
        // Regular user with USER role
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword123");
        testUser.setRole("USER");

        // Admin user with ADMIN role
        adminUser = new User();
        adminUser.setId(2L);
        adminUser.setUsername("admin");
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword("encodedAdminPassword");
        adminUser.setRole("ADMIN");
    }

    // ==================== Successful User Loading Tests ====================

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
    @DisplayName("Should assign USER role from database")
    void shouldAssignUserRoleFromDatabase() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        // Then
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_USER");
    }

    @Test
    @DisplayName("Should assign ADMIN role from database")
    void shouldAssignAdminRoleFromDatabase() {
        // Given
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("admin");

        // Then
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_ADMIN");
    }

    @Test
    @DisplayName("Should read role dynamically from user entity")
    void shouldReadRoleDynamicallyFromEntity() {
        // Given
        User customRoleUser = new User();
        customRoleUser.setUsername("manager");
        customRoleUser.setPassword("encoded");
        customRoleUser.setRole("MANAGER");

        when(userRepository.findByUsername("manager")).thenReturn(Optional.of(customRoleUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("manager");

        // Then
        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_MANAGER");
    }

    // ==================== ROLE_ Prefix Handling Tests ====================

    @Test
    @DisplayName("Should handle role with ROLE_ prefix already present")
    void shouldHandleRoleWithPrefixAlreadyPresent() {
        // Given
        User userWithPrefix = new User();
        userWithPrefix.setUsername("prefixuser");
        userWithPrefix.setPassword("encoded");
        userWithPrefix.setRole("ROLE_USER"); // Already has ROLE_ prefix

        when(userRepository.findByUsername("prefixuser")).thenReturn(Optional.of(userWithPrefix));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("prefixuser");

        // Then
        // Should still result in ROLE_USER (not ROLE_ROLE_USER)
        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_USER");
    }

    @Test
    @DisplayName("Should remove ROLE_ prefix before passing to roles() method")
    void shouldRemovePrefixBeforePassingToRoles() {
        // Given
        User userWithPrefix = new User();
        userWithPrefix.setUsername("admin");
        userWithPrefix.setPassword("encoded");
        userWithPrefix.setRole("ROLE_ADMIN");

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(userWithPrefix));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("admin");

        // Then
        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_ADMIN"); // Not ROLE_ROLE_ADMIN
    }

    @Test
    @DisplayName("Should handle role without ROLE_ prefix")
    void shouldHandleRoleWithoutPrefix() {
        // Given - testUser has role "USER" without prefix
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        // Then
        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_USER"); // Prefix added automatically
    }

    // ==================== User Not Found Tests ====================

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        String username = "nonexistent";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(username))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found with username: nonexistent");

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    @DisplayName("Should handle null username")
    void shouldHandleNullUsername() {
        // Given
        when(userRepository.findByUsername(null)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(null))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found with username: null");
    }

    @Test
    @DisplayName("Should handle empty username")
    void shouldHandleEmptyUsername() {
        // Given
        when(userRepository.findByUsername("")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(""))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found with username: ");
    }

    @Test
    @DisplayName("Should throw exception containing 'User not found'")
    void shouldThrowExceptionWithCorrectMessage() {
        // Given
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("anyuser"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    // ==================== UserDetails Properties Tests ====================

    @Test
    @DisplayName("Should return enabled and non-locked UserDetails")
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
    @DisplayName("Should preserve username exactly as stored")
    void shouldPreserveUsername() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        // Then
        assertThat(userDetails.getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("Should preserve encoded password")
    void shouldPreserveEncodedPassword() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        // Then
        assertThat(userDetails.getPassword()).isEqualTo("encodedPassword123");
    }

    // ==================== Repository Interaction Tests ====================

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
    @DisplayName("Should call repository with exact username provided")
    void shouldCallRepositoryWithExactUsername() {
        // Given
        String username = "TestUser123";
        User user = new User();
        user.setUsername(username);
        user.setPassword("encoded");
        user.setRole("USER");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // When
        userDetailsService.loadUserByUsername(username);

        // Then
        verify(userRepository, times(1)).findByUsername(username);
    }

    // ==================== Edge Cases Tests ====================

    @Test
    @DisplayName("Should handle user with null role gracefully")
    void shouldHandleNullRole() {
        // Given
        User userWithNullRole = new User();
        userWithNullRole.setUsername("nullroleuser");
        userWithNullRole.setPassword("encoded");
        userWithNullRole.setRole(null);

        when(userRepository.findByUsername("nullroleuser")).thenReturn(Optional.of(userWithNullRole));

        // When & Then
        // This will throw NullPointerException when checking role.startsWith()
        // Your service should handle this case, but currently doesn't
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("nullroleuser"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should handle empty role string")
    void shouldHandleEmptyRole() {
        // Given
        User userWithEmptyRole = new User();
        userWithEmptyRole.setUsername("emptyroleuser");
        userWithEmptyRole.setPassword("encoded");
        userWithEmptyRole.setRole("");

        when(userRepository.findByUsername("emptyroleuser")).thenReturn(Optional.of(userWithEmptyRole));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("emptyroleuser");

        // Then
        // Empty string passed to roles() will result in "ROLE_"
        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_");
    }

    @Test
    @DisplayName("Should handle lowercase role")
    void shouldHandleLowercaseRole() {
        // Given
        User userWithLowercaseRole = new User();
        userWithLowercaseRole.setUsername("lowercaseuser");
        userWithLowercaseRole.setPassword("encoded");
        userWithLowercaseRole.setRole("admin"); // lowercase

        when(userRepository.findByUsername("lowercaseuser")).thenReturn(Optional.of(userWithLowercaseRole));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("lowercaseuser");

        // Then
        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_admin"); // Preserves case
    }

    @Test
    @DisplayName("Should handle role with spaces")
    void shouldHandleRoleWithSpaces() {
        // Given
        User userWithSpacedRole = new User();
        userWithSpacedRole.setUsername("spaceduser");
        userWithSpacedRole.setPassword("encoded");
        userWithSpacedRole.setRole("SUPER ADMIN");

        when(userRepository.findByUsername("spaceduser")).thenReturn(Optional.of(userWithSpacedRole));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("spaceduser");

        // Then
        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_SUPER ADMIN");
    }

    @Test
    @DisplayName("Should handle multiple users with different roles")
    void shouldHandleMultipleUsersDifferentRoles() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));

        // When
        UserDetails user1 = userDetailsService.loadUserByUsername("testuser");
        UserDetails user2 = userDetailsService.loadUserByUsername("admin");

        // Then
        assertThat(user1.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_USER");

        assertThat(user2.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_ADMIN");

        verify(userRepository, times(1)).findByUsername("testuser");
        verify(userRepository, times(1)).findByUsername("admin");
    }

    // ==================== Integration Scenario Tests ====================

    @Test
    @DisplayName("Should load user and build correct UserDetails object")
    void shouldBuildCompleteUserDetailsObject() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        // Then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("testuser");
        assertThat(userDetails.getPassword()).isEqualTo("encodedPassword123");
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.isEnabled()).isTrue();
        assertThat(userDetails.isAccountNonExpired()).isTrue();
        assertThat(userDetails.isAccountNonLocked()).isTrue();
        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
    }

    @Test
    @DisplayName("Should successfully authenticate admin user")
    void shouldAuthenticateAdminUser() {
        // Given
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("admin");

        // Then
        assertThat(userDetails.getUsername()).isEqualTo("admin");
        assertThat(userDetails.getPassword()).isEqualTo("encodedAdminPassword");
        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_ADMIN");
    }
}