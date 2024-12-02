package com.example.fitnessclubmanagementsystem.controllers;

import com.example.fitnessclubmanagementsystem.models.Member;
import com.example.fitnessclubmanagementsystem.models.Trainer;
import com.example.fitnessclubmanagementsystem.models.Schedule;
import com.example.fitnessclubmanagementsystem.repositories.MemberRepository;
import com.example.fitnessclubmanagementsystem.repositories.TrainerRepository;
import com.example.fitnessclubmanagementsystem.repositories.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * AdminController handles all operations for the admin role.
 * This includes managing members, trainers, and schedules.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Displays the admin dashboard.
     *
     * @return The view for the admin dashboard.
     */
    @GetMapping("/dashboard")
    public String showAdminDashboard() {
        return "admin-dashboard";
    }

    // -------------------- Member Management --------------------

    /**
     * Displays all members for management.
     *
     * @param model Adds the list of members to the view.
     * @return The view for managing members.
     */
    @GetMapping("/members")
    public String manageMembers(Model model) {
        model.addAttribute("members", memberRepository.findAll());
        return "admin-members";
    }

    /**
     * Displays the form to add a new member.
     *
     * @param model Adds a new Member object to the form.
     * @return The view for the member form.
     */
    @GetMapping("/members/add")
    public String showAddMemberForm(Model model) {
        model.addAttribute("member", new Member());
        return "admin-member-form";
    }

    /**
     * Saves a new or updated member.
     *
     * @param member The member details from the form.
     * @param redirectAttributes Attributes to pass success or error messages.
     * @return Redirects to the manage members page.
     */
    @PostMapping("/members/save")
    public String saveMember(@ModelAttribute Member member, RedirectAttributes redirectAttributes) {
        if (member.getId() == null || (member.getPassword() != null && !member.getPassword().isEmpty())) {
            member.setPassword(passwordEncoder.encode(member.getPassword())); // Hash password for new or updated member
        } else {
            // Retain old password if it's not being updated
            Member existingMember = memberRepository.findById(member.getId()).orElse(null);
            if (existingMember != null) {
                member.setPassword(existingMember.getPassword());
            }
        }
        memberRepository.save(member);
        redirectAttributes.addFlashAttribute("success", "Member saved successfully!");
        return "redirect:/admin/members";
    }

    /**
     * Displays the form to edit an existing member.
     *
     * @param id The ID of the member to edit.
     * @param model Adds the member to the form.
     * @param redirectAttributes Attributes to pass error messages.
     * @return The view for the member form or redirects if member is not found.
     */
    @GetMapping("/members/edit/{id}")
    public String showEditMemberForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Member member = memberRepository.findById(id).orElse(null);
        if (member == null) {
            redirectAttributes.addFlashAttribute("error", "Member not found!");
            return "redirect:/admin/members";
        }
        model.addAttribute("member", member);
        return "admin-member-form";
    }

    /**
     * Deletes a member.
     * @param id The ID of the member to delete.
     * @param redirectAttributes Attributes to pass success or error messages.
     * @return Redirects to the manage members page.
     */
    @GetMapping("/members/delete/{id}")
    public String deleteMember(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            memberRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Member deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot delete member. They are assigned to one or more schedules.");
        }
        return "redirect:/admin/members";
    }

    // -------------------- Trainer Management --------------------

    // Similar methods for trainers as for members
    @GetMapping("/trainers")
    public String manageTrainers(Model model) {
        model.addAttribute("trainers", trainerRepository.findAll());
        return "admin-trainers";
    }

    @GetMapping("/trainers/add")
    public String showAddTrainerForm(Model model) {
        model.addAttribute("trainer", new Trainer());
        return "admin-trainer-form";
    }

    @PostMapping("/trainers/save")
    public String saveTrainer(@ModelAttribute Trainer trainer, RedirectAttributes redirectAttributes) {
        if (trainer.getId() == null || (trainer.getPassword() != null && !trainer.getPassword().isEmpty())) {
            trainer.setPassword(passwordEncoder.encode(trainer.getPassword()));
        } else {
            Trainer existingTrainer = trainerRepository.findById(trainer.getId()).orElse(null);
            if (existingTrainer != null) {
                trainer.setPassword(existingTrainer.getPassword());
            }
        }
        trainerRepository.save(trainer);
        redirectAttributes.addFlashAttribute("success", "Trainer saved successfully!");
        return "redirect:/admin/trainers";
    }

    @GetMapping("/trainers/edit/{id}")
    public String showEditTrainerForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Trainer trainer = trainerRepository.findById(id).orElse(null);
        if (trainer == null) {
            redirectAttributes.addFlashAttribute("error", "Trainer not found!");
            return "redirect:/admin/trainers";
        }
        model.addAttribute("trainer", trainer);
        return "admin-trainer-form";
    }

    @GetMapping("/trainers/delete/{id}")
    public String deleteTrainer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            trainerRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Trainer deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot delete trainer. They are assigned to one or more schedules.");
        }
        return "redirect:/admin/trainers";
    }

    // -------------------- Schedule Management --------------------

    @GetMapping("/schedules")
    public String manageSchedules(Model model) {
        model.addAttribute("schedules", scheduleRepository.findAll());
        return "admin-schedules";
    }

    @GetMapping("/schedules/add")
    public String showAddScheduleForm(Model model) {
        model.addAttribute("schedule", new Schedule());
        model.addAttribute("trainers", trainerRepository.findAll());
        model.addAttribute("members", memberRepository.findAll());
        return "admin-schedule-form";
    }

    @PostMapping("/schedules/save")
    public String saveSchedule(@ModelAttribute Schedule schedule, RedirectAttributes redirectAttributes) {
        scheduleRepository.save(schedule);
        redirectAttributes.addFlashAttribute("success", "Schedule saved successfully!");
        return "redirect:/admin/schedules";
    }

    @GetMapping("/schedules/edit/{id}")
    public String showEditScheduleForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Schedule schedule = scheduleRepository.findById(id).orElse(null);
        if (schedule == null) {
            redirectAttributes.addFlashAttribute("error", "Schedule not found!");
            return "redirect:/admin/schedules";
        }
        model.addAttribute("schedule", schedule);
        model.addAttribute("trainers", trainerRepository.findAll());
        model.addAttribute("members", memberRepository.findAll());
        return "admin-schedule-form";
    }

    @GetMapping("/schedules/delete/{id}")
    public String deleteSchedule(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        scheduleRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Schedule deleted successfully!");
        return "redirect:/admin/schedules";
    }
}
