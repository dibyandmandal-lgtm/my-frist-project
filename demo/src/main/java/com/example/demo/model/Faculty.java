package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "faculty")
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String fullName;
    private String employeeId;
    private String subject;
    private String department;
    private String qualification;
    private Integer experience;
    private String email;
    private String password;
    private String phone;

    // ===== GETTERS & SETTERS =====


}
