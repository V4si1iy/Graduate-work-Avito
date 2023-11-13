package ru.skypro.homework.model.entity;

import lombok.Data;
import ru.skypro.homework.model.dto.Role;

import javax.persistence.*;

@Entity
@Table(name = "mp_users")
@Data
public class UserModel {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
    private String image;
}
