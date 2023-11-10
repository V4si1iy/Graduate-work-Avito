package ru.skypro.homework.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
@Data
@RequiredArgsConstructor
public class Ads {
    private int count;
    private Collection<Ad> results;
}
