package com.example.demo.dto;

public class RegisterDTO {

    private String name;
    private String university;
    private String email;
    private String password;

    public String getName() {
        return name;
    }

    public String getUniversity() {
        return university;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
