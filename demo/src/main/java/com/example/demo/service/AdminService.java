package com.example.demo.service;

import com.example.demo.dto.AdminDTO;

import com.example.demo.model.Admin;

import com.example.demo.repository.AdminRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;



    public Admin adminlogin(AdminDTO dto) {
        List<Admin> admins = adminRepository.findAllByEmail(dto.getEmail());
        for (Admin admin : admins) {
            if (admin.getPassword().equals(dto.getPassword())) {
                return admin;
            }
        }
        return null;
    }
}
