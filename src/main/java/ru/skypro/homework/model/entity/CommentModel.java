package ru.skypro.homework.model.entity;

import lombok.Data;
import ru.skypro.homework.model.entity.AdModel;
import ru.skypro.homework.model.entity.UserModel;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "mp_comments")
@Data
public class CommentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;
    @ManyToOne
    @JoinColumn(name = "ads_id")
    private AdModel ads;
    private LocalDate createdAt;
    private String text;
}