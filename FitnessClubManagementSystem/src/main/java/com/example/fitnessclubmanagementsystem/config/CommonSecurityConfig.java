package com.example.fitnessclubmanagementsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Common security configuration shared across the application.
 * Provides a PasswordEncoder bean for secure password hashing.
 */
@Configuration
public class CommonSecurityConfig {

    /**
     * Configures the BCrypt password encoder.
     *
     * @return A PasswordEncoder instance using BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
