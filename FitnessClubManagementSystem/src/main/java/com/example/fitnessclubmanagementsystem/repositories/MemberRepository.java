package com.example.fitnessclubmanagementsystem.repositories;

import com.example.fitnessclubmanagementsystem.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);
}
