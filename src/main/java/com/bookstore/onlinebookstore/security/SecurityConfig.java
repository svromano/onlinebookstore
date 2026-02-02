package com.bookstore.onlinebookstore.security;

import com.bookstore.onlinebookstore.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Main configuration class for Spring Security.
 * @Configuration: Tells Spring to use this class to set up security beans.
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    /**
     * Defines the security rules for HTTP requests.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF (Cross-Site Request Forgery) protection for easier API testing
                .csrf(csrf -> csrf.disable())

                // Permission Rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login.html",      // Allow everyone to see the login page
                                "/register.html",   // Allow everyone to see the register page
                                "/api/auth/**",     // Allow access to login/register endpoints
                                "/css/**",          // Allow static styles
                                "/js/**"            // Allow frontend scripts
                        ).permitAll()
                        .anyRequest().authenticated() // All other pages (like /cart.html) require login
                )

                // Login Configuration
                .formLogin(form -> form
                        .loginPage("/login.html")              // Custom UI for logging in
                        .loginProcessingUrl("/api/auth/login") // The URL the frontend POSTs to
                        .defaultSuccessUrl("/catalog.html", true) // Where to go after success
                        .failureUrl("/login.html?error=true")  // Where to go if login fails
                )

                // Logout Configuration
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessUrl("/login.html")
                        .permitAll()
                );

        return http.build();
    }

    /**
     * Bean used by AuthService to hash passwords.
     * BCrypt is a standard, strong hashing algorithm.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Exposes the AuthenticationManager to handle the login logic.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}