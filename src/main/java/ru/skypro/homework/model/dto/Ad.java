package ru.skypro.homework.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@RequiredArgsConstructor
public class Ad {
    private Long author;
    private String image;
    @Id
    private Long pk;
    private int price;
    private String title;
    private String description;
}
