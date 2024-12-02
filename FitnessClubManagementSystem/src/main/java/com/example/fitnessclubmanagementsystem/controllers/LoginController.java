package com.example.fitnessclubmanagementsystem.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles login-related operations.
 * Displays the login page and manages error or logout messages.
 */
@Controller
public class LoginController {

    /**
     * Displays the login page.
     *
     * @param error Optional parameter for login error messages.
     * @param logout Optional parameter for logout success messages.
     * @param model Used to pass error or success messages to the view.
     * @return The view for the login page.
     */
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {

        if (error != null) {
            model.addAttribute("error", "Invalid username or password. Please try again.");
        }

        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }

        return "login";
    }
}
