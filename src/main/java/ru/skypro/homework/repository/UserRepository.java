package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.dto.User;
import ru.skypro.homework.model.entity.UserModel;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<UserModel,Long> {


 UserModel getUserModelByUsername(String username);
Optional<UserModel> findByUsername(String login);
}
