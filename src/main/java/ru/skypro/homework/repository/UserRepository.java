package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.entity.UserModel;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel,Long> {


  public UserModel getUserModelByUsername(String username);
}
