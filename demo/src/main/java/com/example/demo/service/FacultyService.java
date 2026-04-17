package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.FacultyDTO;
import com.example.demo.model.Faculty;
import com.example.demo.repository.FacultyRepository;

import java.util.List;

@Service
public class FacultyService {

    @Autowired
    private FacultyRepository facultyRepository;

    // DTO → Entity convert
    public Faculty convertToEntity(FacultyDTO dto) {
        Faculty f = new Faculty();

        f.setFullName(dto.getFullName());
        f.setEmployeeId(dto.getEmployeeId());
        f.setSubject(dto.getSubject());
        f.setDepartment(dto.getDepartment());
        f.setQualification(dto.getQualification());
        f.setExperience(dto.getExperience());
        f.setEmail(dto.getEmail());
        f.setPassword(dto.getPassword());
        f.setPhone(dto.getPhone());

        return f;
    }

    // Save faculty
    public Faculty saveFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    // Get all faculty
    public List<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }
}