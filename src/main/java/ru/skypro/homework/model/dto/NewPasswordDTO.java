package ru.skypro.homework.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class NewPasswordDTO {
    private final static int minLength = 8;
    private final static int maxLength = 16;
    private String currentPassword;
    private String newPassword;
}
