package com.example.fitnessclubmanagementsystem.repositories;

import com.example.fitnessclubmanagementsystem.models.Schedule;
import com.example.fitnessclubmanagementsystem.models.Member;
import com.example.fitnessclubmanagementsystem.models.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByTrainer(Trainer trainer);

    List<Schedule> findByMember(Member member);
}
