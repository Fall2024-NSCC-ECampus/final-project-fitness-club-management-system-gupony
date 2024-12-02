package com.example.fitnessclubmanagementsystem.repositories;

import com.example.fitnessclubmanagementsystem.models.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
}
