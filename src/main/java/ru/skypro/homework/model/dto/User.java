package ru.skypro.homework.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

@Data
@RequiredArgsConstructor
public class User {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
    private String image;
}
