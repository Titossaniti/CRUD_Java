package com.example.democrud.model;

import lombok.Data;

@Data
public class RegisterUserModel {
    private String email;
    private String password;
    private String name;
    private int age;
}