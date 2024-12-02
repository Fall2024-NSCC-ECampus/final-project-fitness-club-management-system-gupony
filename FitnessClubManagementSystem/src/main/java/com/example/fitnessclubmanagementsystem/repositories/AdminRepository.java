package com.example.fitnessclubmanagementsystem.repositories;

import com.example.fitnessclubmanagementsystem.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
