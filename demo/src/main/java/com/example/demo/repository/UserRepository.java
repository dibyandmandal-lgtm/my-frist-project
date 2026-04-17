package com.example.demo.repository;



import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    /** Returns all users with this email (handles duplicate emails in DB). */
    List<User> findAllByEmail(String email);

}
