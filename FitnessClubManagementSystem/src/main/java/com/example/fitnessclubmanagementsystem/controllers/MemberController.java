package com.example.fitnessclubmanagementsystem.controllers;

import com.example.fitnessclubmanagementsystem.models.Member;
import com.example.fitnessclubmanagementsystem.models.Schedule;
import com.example.fitnessclubmanagementsystem.repositories.MemberRepository;
import com.example.fitnessclubmanagementsystem.repositories.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Handles member-related operations, including viewing schedules and resetting passwords.
 */
@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Displays the member dashboard.
     *
     * @param authentication Provides the member's authentication details.
     * @param model Adds the welcome message for the member.
     * @return The member dashboard view.
     */
    @GetMapping("/dashboard")
    public String showMemberDashboard(Authentication authentication, Model model) {
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email);

        String welcomeMessage = (member != null) ? member.getName() : "Member";
        model.addAttribute("welcomeMessage", welcomeMessage);

        return "member-dashboard";
    }

    /**
     * Displays the logged-in member's schedule.
     *
     * @param authentication Provides the member's authentication details.
     * @param model Adds the member's specific schedules to the view.
     * @return The member schedule view.
     */
    @GetMapping("/schedule")
    public String viewSchedule(Authentication authentication, Model model) {
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email);

        if (member != null) {
            model.addAttribute("schedules", scheduleRepository.findByMember(member));
        } else {
            model.addAttribute("schedules", List.of());
        }

        return "member-schedule";
    }

    /**
     * Displays the password reset form.
     *
     * @return The password reset form view.
     */
    @GetMapping("/reset-password")
    public String resetPasswordForm() {
        return "member-reset-password";
    }

    /**
     * Handles password reset for the member.
     *
     * @param currentPassword The current password entered by the member.
     * @param newPassword The new password to be set.
     * @param authentication Provides the member's authentication details.
     * @param model Adds success or error messages to the view.
     * @return Redirects to the dashboard on success or reloads the form on error.
     */
    @PostMapping("/reset-password")
    public String handlePasswordReset(@RequestParam("currentPassword") String currentPassword,
                                      @RequestParam("newPassword") String newPassword,
                                      Authentication authentication,
                                      Model model) {
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            model.addAttribute("error", "User not found.");
            return "member-reset-password";
        }

        // Validate the current password using PasswordEncoder
        if (!passwordEncoder.matches(currentPassword, member.getPassword())) {
            model.addAttribute("error", "Current password is incorrect.");
            return "member-reset-password";
        }

        // Hash the new password and update it
        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);

        model.addAttribute("success", "Password updated successfully!");
        return "redirect:/member/dashboard";
    }
}
