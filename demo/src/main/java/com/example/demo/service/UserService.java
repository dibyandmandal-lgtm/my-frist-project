package com.example.demo.service;


import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User register(RegisterDTO dto) {
        if (!userRepository.findAllByEmail(dto.getEmail()).isEmpty()) {
            throw new RuntimeException("Email already registered");
        }
        User user = new User();
        user.setName(dto.getName());
        user.setUniversity(dto.getUniversity());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return userRepository.save(user);
    }

    public User login(LoginDTO dto) {
        List<User> users = userRepository.findAllByEmail(dto.getEmail());
        for (User user : users) {
            if (user.getPassword().equals(dto.getPassword())) {
                return user;
            }
        }
        return null;
    }
}
