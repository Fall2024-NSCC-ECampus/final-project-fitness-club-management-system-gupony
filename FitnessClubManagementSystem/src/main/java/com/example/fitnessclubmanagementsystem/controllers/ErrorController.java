package com.example.fitnessclubmanagementsystem.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Handles error scenarios in the application.
 * Displays a user-friendly error page when access is denied or other errors occur.
 */
@Controller
public class ErrorController {

    /**
     * Displays a custom error page.
     * @param model Used to pass the error message to the view.
     * @return The view for the error page.
     */
    @GetMapping("/error-page")
    public String showErrorPage(Model model) {
        model.addAttribute("errorMessage", "You do not have permission to access this page.");
        return "error-page";
    }
}
