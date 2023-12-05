package ru.skypro.homework.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "mp_comments")
@Data
public class CommentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_username")
    private UserModel user;
    @ManyToOne
    @JoinColumn(name = "ads_id")
    private AdModel ads;
    private LocalDateTime createdAt;
    private String text;
}
