package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "students")
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String rollNo;   // ✅ ekbar thakbe

    private String firstName;
    private String lastName;

    private LocalDate dateOfBirth;

    private String gender;

    private String branch;
    private String section;

    private String parentName;
    private String contactNumber;

    private String specialisation;
    private String department;

    @Column(length = 500)
    private String address;

    private String gmail;
    private String password;


}