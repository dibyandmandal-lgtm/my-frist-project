package com.example.demo.repository;

import com.example.demo.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacultyRepository extends JpaRepository <Faculty , Integer> {
    Optional<Faculty> findFirstByEmailIgnoreCase(String email);
    Optional<Faculty> findFirstByEmployeeIdIgnoreCase(String employeeId);
}
