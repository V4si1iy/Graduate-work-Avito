package ru.skypro.homework.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UpdateUser {
    private String firstName;
    private String lastName;
    private String phone;
}
