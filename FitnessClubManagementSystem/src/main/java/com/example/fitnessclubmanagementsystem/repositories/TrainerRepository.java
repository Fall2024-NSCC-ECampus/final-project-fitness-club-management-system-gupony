package com.example.fitnessclubmanagementsystem.repositories;

import com.example.fitnessclubmanagementsystem.models.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Trainer findByName(String name);
}
