package com.example.demo.dto;

import jakarta.persistence.Entity;
import lombok.Data;

import java.time.LocalDate;
@Data
public class StudentDTO {

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String rollNo;
    private String gender;
    private String branch;
    private String section;
    private String parentName;
    private String contactNumber;
    private String specialisation;
    private String department;
    private String address;
    private String gmail;
    private String password;




}
