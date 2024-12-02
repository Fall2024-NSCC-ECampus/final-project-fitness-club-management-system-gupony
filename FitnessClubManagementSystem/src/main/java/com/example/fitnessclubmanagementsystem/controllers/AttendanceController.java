package com.example.fitnessclubmanagementsystem.controllers;

import com.example.fitnessclubmanagementsystem.models.Attendance;
import com.example.fitnessclubmanagementsystem.repositories.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles attendance-related operations.
 * Trainers use this controller to manage attendance records for members.
 */
@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepository; // Repository to manage attendance data

    /**
     * Retrieves all attendance records.
     *
     * @return A list of all attendance records in the system.
     */
    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendance() {
        return ResponseEntity.ok(attendanceRepository.findAll());
    }

    /**
     * Retrieves a specific attendance record by its ID.
     *
     * @param id The ID of the attendance record.
     * @return The attendance record if found, or a 404 status if not.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Attendance> getAttendanceById(@PathVariable Long id) {
        return attendanceRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    /**
     * Creates a new attendance record.
     *
     * @param attendance The attendance record to save.
     * @return The created attendance record with a 201 status.
     */
    @PostMapping
    public ResponseEntity<Attendance> createAttendance(@RequestBody Attendance attendance) {
        return ResponseEntity.status(HttpStatus.CREATED).body(attendanceRepository.save(attendance));
    }

    /**
     * Updates an existing attendance record.
     *
     * @param id The ID of the attendance record to update.
     * @param updatedAttendance The updated attendance details.
     * @return The updated attendance record if found, or a 404 status if not.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Attendance> updateAttendance(@PathVariable Long id, @RequestBody Attendance updatedAttendance) {
        return attendanceRepository.findById(id)
                .map(attendance -> {
                    attendance.setMemberId(updatedAttendance.getMemberId());
                    attendance.setTrainerId(updatedAttendance.getTrainerId());
                    attendance.setDate(updatedAttendance.getDate());
                    attendance.setPresent(updatedAttendance.isPresent());
                    return ResponseEntity.ok(attendanceRepository.save(attendance));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    /**
     * Deletes an attendance record by its ID.
     *
     * @param id The ID of the attendance record to delete.
     * @return A success message if deleted, or a 404 status if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAttendance(@PathVariable Long id) {
        if (attendanceRepository.existsById(id)) {
            attendanceRepository.deleteById(id);
            return ResponseEntity.ok("Attendance record deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Attendance record not found.");
    }
}
