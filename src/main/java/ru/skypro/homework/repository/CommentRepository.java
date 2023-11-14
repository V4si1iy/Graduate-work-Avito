package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.entity.AdModel;
import ru.skypro.homework.model.entity.CommentModel;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentModel, Long> {
    public Integer countByAds_Id(Long adId);

    public List<CommentModel> getAllByAds_Id(Long adId);
}
