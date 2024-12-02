package com.example.fitnessclubmanagementsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Configures security specifically for the Admin role.
 * Sets up an in-memory user with the Admin role for simplicity.
 */
@Configuration
public class AdminSecurityConfig {

    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor that injects the PasswordEncoder bean.
     *
     * @param passwordEncoder The PasswordEncoder used for password hashing.
     */
    public AdminSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates an in-memory user details service for the Admin role.
     *
     * @return InMemoryUserDetailsManager configured with the Admin user.
     */
    @Bean(name = "adminUserDetailsService")
    public InMemoryUserDetailsManager adminUserDetailsService() {
        return new InMemoryUserDetailsManager(
                org.springframework.security.core.userdetails.User.builder()
                        .username("admin") // Hardcoded username for the Admin
                        .password(passwordEncoder.encode("adminpass"))
                        .roles("ADMIN")
                        .build()
        );
    }
}
