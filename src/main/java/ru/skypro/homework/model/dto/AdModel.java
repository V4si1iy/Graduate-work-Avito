package ru.skypro.homework.model.dto;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "mp_ads")
@Data
public class AdModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String image;
    private int price;
    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;
}
