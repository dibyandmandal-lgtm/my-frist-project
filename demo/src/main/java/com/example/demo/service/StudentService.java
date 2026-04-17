package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.StudentDTO;
import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // DTO → Entity convert
    public Student convertToEntity(StudentDTO dto) {
        Student s = new Student();

        s.setFirstName(dto.getFirstName());
        s.setLastName(dto.getLastName());
        s.setDateOfBirth(dto.getDateOfBirth());
        s.setGender(dto.getGender());
        s.setBranch(dto.getBranch());
        s.setSection(dto.getSection());
        s.setParentName(dto.getParentName());
        s.setContactNumber(dto.getContactNumber());
        s.setSpecialisation(dto.getSpecialisation());
        s.setRollNo(dto.getRollNo());
        s.setDepartment(dto.getDepartment());
        s.setAddress(dto.getAddress());
        s.setGmail(dto.getGmail());
        s.setPassword(dto.getPassword());

        return s;
    }

    // Save student
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    // Get all students
    public java.util.List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}