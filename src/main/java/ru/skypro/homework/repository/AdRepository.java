package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.skypro.homework.model.dto.Ads;
import ru.skypro.homework.model.entity.AdModel;
import ru.skypro.homework.model.entity.UserModel;

import java.util.List;

public interface AdRepository extends JpaRepository<AdModel,Long> {
    public Integer countByUser(UserModel userModel);
    public List<AdModel> getAdModelByUser(UserModel userModel);
}
