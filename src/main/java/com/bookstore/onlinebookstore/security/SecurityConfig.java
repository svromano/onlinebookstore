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
 * The type Security config.
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    /**
     * Filter chain security filter chain.
     *
     * @param http the http
     * @return the security filter chain
     * @throws Exception the exception
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
     * Password encoder password encoder.
     *
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication manager authentication manager.
     *
     * @param config the config
     * @return the authentication manager
     * @throws Exception the exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}