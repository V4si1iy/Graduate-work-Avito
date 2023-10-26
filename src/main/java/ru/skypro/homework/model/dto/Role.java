package ru.skypro.homework.model.dto;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum Role {
    USER("Базовая роль пользователя"),
    ADMIN("Главная роль пользователя со всеми правами");
    private String description;


}
