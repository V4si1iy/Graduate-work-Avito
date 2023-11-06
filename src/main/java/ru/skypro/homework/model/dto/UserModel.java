package ru.skypro.homework.model.dto;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "mp_users")
@Data
public class UserModel {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
}
