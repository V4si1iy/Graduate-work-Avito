package ru.skypro.homework.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Register {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
}
