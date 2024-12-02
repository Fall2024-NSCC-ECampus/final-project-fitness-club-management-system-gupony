package com.example.fitnessclubmanagementsystem.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Represents a gym member in the system.
 * Members can log in, view their schedules, and update their details.
 */
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String membershipType;
    private String password;

    public Member() {
    }

    /**
     * Constructor to create a Member with specified details.
     *
     * @param name The member's full name.
     * @param email The member's email address.
     * @param membershipType The type of membership.
     * @param password The hashed password for secure login.
     */
    public Member(String name, String email, String membershipType, String password) {
        this.name = name;
        this.email = email;
        this.membershipType = membershipType;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
