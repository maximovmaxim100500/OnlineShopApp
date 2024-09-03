package ru.skypro.homework.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.db.entity.Ad;
import ru.skypro.homework.db.entity.AdImage;

import java.util.Optional;

@Repository
public interface AdImageRepository extends JpaRepository<AdImage, Integer> {

    Optional<AdImage> findById(Integer id);

    Optional<AdImage> findByAd(Ad ad);

}
