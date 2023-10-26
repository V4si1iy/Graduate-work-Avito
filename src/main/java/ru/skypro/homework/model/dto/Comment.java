package ru.skypro.homework.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class Comment {
    private Long author;
    private String authorImage;
    private LocalDate createdAt;
    private Long pk;
    private String text;
}
