package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Image getImageByLink(String link);

    Image getById(String id);
}
