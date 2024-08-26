package ru.skypro.homework.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.db.entity.Ad;
import ru.skypro.homework.db.entity.User;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {
    List<Ad> findByUser(User user);

}
