package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.entity.CommentModel;

public interface CommentRepository extends JpaRepository<CommentModel, Long> {

}
