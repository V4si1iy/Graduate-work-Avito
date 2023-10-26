package ru.skypro.homework.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Ad {
    private Long author;
    private String image;
    private Long pk;
    private int price;
    private String title;
}
