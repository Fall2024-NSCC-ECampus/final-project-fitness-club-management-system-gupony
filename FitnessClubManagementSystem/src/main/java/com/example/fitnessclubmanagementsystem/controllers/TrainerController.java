package com.example.fitnessclubmanagementsystem.controllers;

import com.example.fitnessclubmanagementsystem.models.Attendance;
import com.example.fitnessclubmanagementsystem.models.Member;
import com.example.fitnessclubmanagementsystem.models.Schedule;
import com.example.fitnessclubmanagementsystem.models.Trainer;
import com.example.fitnessclubmanagementsystem.repositories.AttendanceRepository;
import com.example.fitnessclubmanagementsystem.repositories.MemberRepository;
import com.example.fitnessclubmanagementsystem.repositories.ScheduleRepository;
import com.example.fitnessclubmanagementsystem.repositories.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Handles trainer-related operations, including managing schedules and attendance.
 */
@Controller
@RequestMapping("/trainer")
public class TrainerController {

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    /**
     * Displays the trainer dashboard.
     */
    @GetMapping("/dashboard")
    public String showTrainerDashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        Trainer trainer = trainerRepository.findByName(username);

        if (trainer == null) {
            model.addAttribute("error", "Trainer not found!");
            return "error-page";
        }

        model.addAttribute("welcomeMessage", trainer.getName());
        return "trainer-dashboard";
    }

    /**
     * Displays the trainer's schedule.
     */
    @GetMapping("/schedule")
    public String viewTrainerSchedule(Authentication authentication, Model model) {
        String trainerName = authentication.getName();
        Trainer trainer = trainerRepository.findByName(trainerName);

        if (trainer == null) {
            model.addAttribute("error", "Trainer not found!");
            return "error-page";
        }

        List<Schedule> schedules = scheduleRepository.findByTrainer(trainer);
        model.addAttribute("schedules", schedules);

        return "trainer-schedule";
    }

    /**
     * Displays the attendance form for marking or editing attendance.
     */
    @GetMapping({"/attendance", "/attendance/edit/{id}"})
    public String markAttendanceForm(@PathVariable(required = false) Long id, Model model, Authentication authentication) {
        String trainerName = authentication.getName();
        Trainer trainer = trainerRepository.findByName(trainerName);

        if (trainer == null) {
            model.addAttribute("error", "Trainer not found!");
            return "error-page";
        }

        Attendance attendance = (id != null) ? attendanceRepository.findById(id).orElse(new Attendance()) : new Attendance();
        model.addAttribute("attendance", attendance);
        model.addAttribute("members", memberRepository.findAll());

        return "trainer-attendance-form";
    }

    /**
     * Saves or updates an attendance record.
     */
    @PostMapping("/attendance")
    public String saveAttendance(@ModelAttribute Attendance attendance, Authentication authentication, RedirectAttributes redirectAttributes) {
        String trainerName = authentication.getName();
        Trainer trainer = trainerRepository.findByName(trainerName);

        if (trainer == null) {
            redirectAttributes.addFlashAttribute("error", "Trainer not found!");
            return "redirect:/trainer/attendance";
        }

        Member member = memberRepository.findById(attendance.getMemberId()).orElse(null);
        if (member == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid member selected!");
            return "redirect:/trainer/attendance";
        }

        if (attendance.getId() != null) {
            Attendance existingAttendance = attendanceRepository.findById(attendance.getId()).orElse(null);
            if (existingAttendance != null) {
                existingAttendance.setMemberId(attendance.getMemberId());
                existingAttendance.setDate(attendance.getDate());
                existingAttendance.setPresent(attendance.isPresent());
                attendanceRepository.save(existingAttendance);
                redirectAttributes.addFlashAttribute("success", "Attendance updated successfully!");
                return "redirect:/trainer/attendance/view";
            }
        }

        attendance.setTrainerId(trainer.getId());
        attendanceRepository.save(attendance);
        redirectAttributes.addFlashAttribute("success", "Attendance marked successfully!");
        return "redirect:/trainer/attendance/view";
    }

    /**
     * Displays all attendance records for the trainer.
     */
    @GetMapping("/attendance/view")
    public String viewAttendance(Authentication authentication, Model model) {
        String trainerName = authentication.getName();
        Trainer trainer = trainerRepository.findByName(trainerName);

        if (trainer == null) {
            model.addAttribute("error", "Trainer not found!");
            return "error-page";
        }

        List<Attendance> attendances = attendanceRepository.findAll();
        attendances.forEach(attendance -> {
            Member member = memberRepository.findById(attendance.getMemberId()).orElse(null);
            if (member != null) {
                attendance.setMemberName(member.getName());
            }
        });

        model.addAttribute("attendances", attendances);
        return "trainer-attendance";
    }

    /**
     * Deletes an attendance record by ID.
     */
    @GetMapping("/attendance/delete/{id}")
    public String deleteAttendance(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (attendanceRepository.existsById(id)) {
            attendanceRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Attendance deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Attendance record not found!");
        }
        return "redirect:/trainer/attendance/view";
    }
}
