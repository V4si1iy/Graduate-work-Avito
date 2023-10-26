package ru.skypro.homework.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ExtendedAd {
    private Long pk;
    private String authorFirstName;
    private String authorLastName;
    private String description;
    private String email;
    private String image;
    private String phone;
    private int price;
    private String title;
}
