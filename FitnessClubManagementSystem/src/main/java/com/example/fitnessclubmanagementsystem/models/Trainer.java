package com.example.fitnessclubmanagementsystem.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Represents a trainer in the fitness club.
 * Trainers can log in, manage schedules, and mark attendance for members.
 */
@Entity
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String specialty; // Trainer's area of expertise (e.g., Yoga, Cardio)
    private String shift; // Trainer's working shift (e.g., Morning, Evening)
    private String password;

    public Trainer() {

    }

    /**
     * Constructor to create a Trainer with specified details.
     *
     * @param name The trainer's full name.
     * @param specialty The trainer's specialty area.
     * @param shift The trainer's shift.
     * @param password The hashed password for secure login.
     */
    public Trainer(String name, String specialty, String shift, String password) {
        this.name = name;
        this.specialty = specialty;
        this.shift = shift;
        this.password = password;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
