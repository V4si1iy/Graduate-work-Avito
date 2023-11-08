package ru.skypro.homework.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class Comment {
    private Long author;
    private String authorImage;
    private LocalDate createdAt;
    @Id
    private Long pk;
    private String text;
}
