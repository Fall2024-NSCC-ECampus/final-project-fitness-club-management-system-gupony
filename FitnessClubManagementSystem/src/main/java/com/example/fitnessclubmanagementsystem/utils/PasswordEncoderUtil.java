package com.example.fitnessclubmanagementsystem.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility class for encoding passwords using BCrypt.
 * Can be used to manually encode passwords for initial user setup.
 */
public class PasswordEncoderUtil {

    public static void main(String[] args) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "password";

        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Encoded Password: " + encodedPassword);
    }
}
