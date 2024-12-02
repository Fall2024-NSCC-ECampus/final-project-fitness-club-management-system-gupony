package com.example.fitnessclubmanagementsystem.config;

import com.example.fitnessclubmanagementsystem.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Main security configuration for the application.
 * Configures role-based access control, custom login, and logout handling.
 */
@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final InMemoryUserDetailsManager adminUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor to inject dependencies.
     *
     * @param customUserDetailsService The service for member and trainer authentication.
     * @param adminUserDetailsService The service for Admin authentication.
     * @param passwordEncoder The PasswordEncoder bean for password hashing.
     */
    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                          InMemoryUserDetailsManager adminUserDetailsService,
                          PasswordEncoder passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.adminUserDetailsService = adminUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Configures HTTP security rules for role-based access control.
     *
     * @param http The HttpSecurity object.
     * @return A configured SecurityFilterChain.
     * @throws Exception In case of any configuration issues.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/trainer/**").hasRole("TRAINER")
                        .requestMatchers("/member/**").hasRole("MEMBER")
                        .requestMatchers("/login", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureHandler((request, response, exception) -> {
                            // Handle login failure with a message
                            request.setAttribute("error", "Invalid username or password. Please try again.");
                            request.getRequestDispatcher("/login").forward(request, response);
                        })
                        .successHandler((request, response, authentication) -> {
                            // Redirect users to their respective dashboards based on roles
                            String role = authentication.getAuthorities().iterator().next().getAuthority();
                            if ("ROLE_ADMIN".equals(role)) {
                                response.sendRedirect("/admin/dashboard");
                            } else if ("ROLE_TRAINER".equals(role)) {
                                response.sendRedirect("/trainer/dashboard");
                            } else if ("ROLE_MEMBER".equals(role)) {
                                response.sendRedirect("/member/dashboard");
                            } else {
                                response.sendRedirect("/login?error=true");
                            }
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/error-page")
                );

        return http.build();
    }

    /**
     * Configures the AuthenticationManager to use custom authentication providers.
     *
     * @param http The HttpSecurity object.
     * @return A configured AuthenticationManager.
     * @throws Exception In case of any configuration issues.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(adminAuthenticationProvider())
                .authenticationProvider(userAuthenticationProvider())
                .build();
    }

    /**
     * Configures the authentication provider for Admins.
     *
     * @return A DaoAuthenticationProvider for Admin authentication.
     */
    @Bean
    public DaoAuthenticationProvider adminAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(adminUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /**
     * Configures the authentication provider for Members and Trainers.
     *
     * @return A DaoAuthenticationProvider for Member and Trainer authentication.
     */
    @Bean
    public DaoAuthenticationProvider userAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
