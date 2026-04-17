package com.example.demo.controller;

import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/student-auth")
@CrossOrigin(origins = "*")
public class StudentAuthController {

    private final StudentRepository studentRepository;

    public StudentAuthController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody StudentLoginRequest body) {
        if (body == null || isBlank(body.getGmail()) || isBlank(body.getPassword())) {
            return ResponseEntity.badRequest().body(message("Gmail and password are required"));
        }

        String gmail = body.getGmail().trim();
        String password = body.getPassword().trim();

        Optional<Student> studentOpt = Optional.empty();
        if (body.getStudentId() != null) {
            studentOpt = studentRepository.findById(body.getStudentId());
            if (studentOpt.isPresent() && !matches(studentOpt.get(), gmail, password)) {
                studentOpt = Optional.empty();
            }
        }

        if (studentOpt.isEmpty()) {
            studentOpt = studentRepository.findFirstByGmailIgnoreCase(gmail)
                    .filter(student -> matches(student, gmail, password));
        }

        if (studentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message("Invalid credentials"));
        }

        Student student = studentOpt.get();
        Map<String, Object> response = new HashMap<>();
        response.put("id", student.getId());
        response.put("firstName", student.getFirstName());
        response.put("lastName", student.getLastName());
        response.put("rollNo", student.getRollNo());
        response.put("gmail", student.getGmail());
        response.put("message", "Login successful");
        return ResponseEntity.ok(response);
    }

    private static boolean matches(Student student, String gmail, String password) {
        if (student == null || isBlank(student.getGmail()) || student.getPassword() == null) {
            return false;
        }
        return student.getGmail().trim().equalsIgnoreCase(gmail) && student.getPassword().equals(password);
    }

    private static Map<String, String> message(String text) {
        Map<String, String> response = new HashMap<>();
        response.put("message", text);
        return response;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static class StudentLoginRequest {
        private Integer studentId;
        private String gmail;
        private String password;

        public StudentLoginRequest() {
        }

        public Integer getStudentId() {
            return studentId;
        }

        public void setStudentId(Integer studentId) {
            this.studentId = studentId;
        }

        public String getGmail() {
            return gmail;
        }

        public void setGmail(String gmail) {
            this.gmail = gmail;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
