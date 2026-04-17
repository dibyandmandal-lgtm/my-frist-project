package com.example.demo.controller;

import com.example.demo.dto.AdminDTO;
import com.example.demo.dto.StudentDTO;
import com.example.demo.dto.FacultyDTO;

import com.example.demo.model.Admin;
import com.example.demo.model.Student;
import com.example.demo.model.Faculty;

import com.example.demo.service.AdminService;
import com.example.demo.service.StudentService;
import com.example.demo.service.FacultyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private FacultyService facultyService;

    // ================= ADMIN LOGIN =================
    @PostMapping("/adminlogin")
    public Admin adminlogin(@RequestBody AdminDTO dto) {
        return adminService.adminlogin(dto);
    }

    // ================= STUDENT =================

    @PostMapping("/add-student")
    public Student addStudent(@RequestBody StudentDTO dto) {
        Student student = studentService.convertToEntity(dto);
        return studentService.saveStudent(student);
    }

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    // ================= FACULTY =================

    // 🔹 Add Faculty
    @PostMapping("/add-faculty")
    public Faculty addFaculty(@RequestBody FacultyDTO dto) {
        Faculty faculty = facultyService.convertToEntity(dto);
        return facultyService.saveFaculty(faculty);
    }

    // 🔹 Get All Faculty
    @GetMapping("/faculty")
    public List<Faculty> getAllFaculty() {
        return facultyService.getAllFaculty();
    }
}