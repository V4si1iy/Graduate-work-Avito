package ru.skypro.homework.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CreateOrUpdateComment {
    private String text;
}
