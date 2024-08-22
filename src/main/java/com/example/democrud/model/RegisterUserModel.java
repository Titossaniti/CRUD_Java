package com.example.democrud.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserModel {
    private String name;
    private int age;
    private String email;
    private String password;
}
