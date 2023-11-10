package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.skypro.homework.model.dto.Ads;
import ru.skypro.homework.model.entity.AdModel;

public interface AdRepository extends JpaRepository<AdModel,Long> {
}
