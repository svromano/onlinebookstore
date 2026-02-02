package com.bookstore.onlinebookstore.service;

import com.bookstore.onlinebookstore.entity.User;
import com.bookstore.onlinebookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of Spring Security's UserDetailsService.
 * This class is responsible for fetching user credentials from the database
 * during the authentication process.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    // Repository used to query the 'users' table
    private final UserRepository userRepository;

    /**
     * Locates the user based on the username provided in the login form.
     * @param username The username entered by the user.
     * @return UserDetails An object that Spring Security uses to perform authentication.
     * @throws UsernameNotFoundException if the user does not exist in the database.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Attempt to find the user in our database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // 2. Map our 'User' entity to Spring Security's 'UserDetails' object.
        // We use the User builder to provide the username, hashed password, and roles.
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // This must be the BCrypt hashed password
                .roles("USER")               // Assigns a default role for authorization
                .build();
    }
}