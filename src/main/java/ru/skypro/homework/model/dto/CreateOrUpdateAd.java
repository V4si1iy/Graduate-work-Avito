package ru.skypro.homework.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CreateOrUpdateAd {
    private String title;
    private int price;
    private String description;
}
