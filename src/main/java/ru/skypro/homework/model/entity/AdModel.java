package ru.skypro.homework.model.entity;

import lombok.Data;
import ru.skypro.homework.model.entity.UserModel;

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
