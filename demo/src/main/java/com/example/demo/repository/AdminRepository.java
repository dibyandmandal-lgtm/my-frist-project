package com.example.demo.repository;

import com.example.demo.model.Admin;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    List<Admin> findAllByEmail(String email);
}
