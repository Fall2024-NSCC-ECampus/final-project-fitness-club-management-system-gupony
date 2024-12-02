package com.example.fitnessclubmanagementsystem.controllers;

import com.example.fitnessclubmanagementsystem.models.Schedule;
import com.example.fitnessclubmanagementsystem.models.Trainer;
import com.example.fitnessclubmanagementsystem.models.Member;
import com.example.fitnessclubmanagementsystem.repositories.ScheduleRepository;
import com.example.fitnessclubmanagementsystem.repositories.TrainerRepository;
import com.example.fitnessclubmanagementsystem.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * Controller for managing schedules.
 */
@Controller
@RequestMapping("/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private MemberRepository memberRepository;

    // -------------------- Admin Functions --------------------

    @GetMapping("/admin")
    public String listSchedulesForAdmin(Model model) {
        model.addAttribute("schedules", scheduleRepository.findAll());
        return "admin-schedules";
    }

    @GetMapping("/admin/add")
    public String showAddScheduleForm(Model model) {
        model.addAttribute("schedule", new Schedule());
        model.addAttribute("trainers", trainerRepository.findAll());
        model.addAttribute("members", memberRepository.findAll());
        return "admin-schedule-form";
    }

    @PostMapping("/admin/save")
    public String saveSchedule(@ModelAttribute Schedule schedule, RedirectAttributes redirectAttributes) {
        Optional<Trainer> trainer = trainerRepository.findById(schedule.getTrainer().getId());
        Optional<Member> member = memberRepository.findById(schedule.getMember().getId());

        if (trainer.isEmpty() || member.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Invalid Trainer or Member selected!");
            return "redirect:/schedules/admin";
        }

        schedule.setTrainer(trainer.get());
        schedule.setMember(member.get());

        scheduleRepository.save(schedule);
        redirectAttributes.addFlashAttribute("success", "Schedule saved successfully!");
        return "redirect:/schedules/admin";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteSchedule(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (scheduleRepository.existsById(id)) {
            scheduleRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Schedule deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Schedule not found!");
        }
        return "redirect:/schedules/admin";
    }

    // -------------------- Trainer Functions --------------------

    @GetMapping("/trainer")
    public String listSchedulesForTrainer(Authentication authentication, Model model) {
        String trainerName = authentication.getName();
        Trainer trainer = trainerRepository.findByName(trainerName);

        if (trainer != null) {
            List<Schedule> schedules = scheduleRepository.findByTrainer(trainer);
            model.addAttribute("schedules", schedules);
        } else {
            model.addAttribute("schedules", List.of());
        }
        return "trainer-schedule";
    }

    // -------------------- Member Functions --------------------

    @GetMapping("/member")
    public String listSchedulesForMember(Authentication authentication, Model model) {
        String memberEmail = authentication.getName();
        Member member = memberRepository.findByEmail(memberEmail);

        if (member != null) {
            List<Schedule> schedules = scheduleRepository.findByMember(member);
            model.addAttribute("schedules", schedules);
        } else {
            model.addAttribute("schedules", List.of());
        }
        return "member-schedule";
    }
}
