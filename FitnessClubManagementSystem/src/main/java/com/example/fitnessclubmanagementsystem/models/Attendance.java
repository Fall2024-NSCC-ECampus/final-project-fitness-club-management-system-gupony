package com.example.fitnessclubmanagementsystem.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

/**
 * Represents attendance records for members.
 * Tracks which member attended, which trainer oversaw it, and the attendance details.
 */
@Entity
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;
    private Long trainerId;
    private String date;
    private boolean present;

    @Transient
    private String memberName;

    public Attendance() {

    }

    /**
     * Constructor to create an attendance record with all details.
     *
     * @param memberId The ID of the member.
     * @param trainerId The ID of the trainer.
     * @param date The date of attendance.
     * @param present Whether the member was present or not.
     */
    public Attendance(Long memberId, Long trainerId, String date, boolean present) {
        this.memberId = memberId;
        this.trainerId = trainerId;
        this.date = date;
        this.present = present;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
}
