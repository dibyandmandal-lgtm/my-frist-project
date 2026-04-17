package com.example.demo.controller;

import com.example.demo.model.Faculty;
import com.example.demo.repository.FacultyRepository;
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
@RequestMapping("/api/faculty-auth")
@CrossOrigin(origins = "*")
public class FacultyAuthController {

    private final FacultyRepository facultyRepository;

    public FacultyAuthController(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody FacultyLoginRequest body) {
        if (body == null || isBlank(body.getIdentifier()) || isBlank(body.getPassword())) {
            return ResponseEntity.badRequest().body(message("Faculty email/employee ID and password are required"));
        }

        String identifier = body.getIdentifier().trim();
        String password = body.getPassword().trim();

        Optional<Faculty> facultyOpt = facultyRepository.findFirstByEmailIgnoreCase(identifier);
        if (facultyOpt.isEmpty()) {
            facultyOpt = facultyRepository.findFirstByEmployeeIdIgnoreCase(identifier);
        }

        if (facultyOpt.isEmpty() || !matches(facultyOpt.get(), password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message("Invalid credentials"));
        }

        Faculty faculty = facultyOpt.get();
        Map<String, Object> response = new HashMap<>();
        response.put("id", faculty.getId());
        response.put("fullName", faculty.getFullName());
        response.put("employeeId", faculty.getEmployeeId());
        response.put("email", faculty.getEmail());
        response.put("subject", faculty.getSubject());
        response.put("department", faculty.getDepartment());
        response.put("message", "Login successful");
        return ResponseEntity.ok(response);
    }

    private static boolean matches(Faculty faculty, String password) {
        return faculty != null && faculty.getPassword() != null && faculty.getPassword().equals(password);
    }

    private static Map<String, String> message(String text) {
        Map<String, String> response = new HashMap<>();
        response.put("message", text);
        return response;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static class FacultyLoginRequest {
        private String identifier;
        private String password;

        public FacultyLoginRequest() {
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
