package ru.skypro.homework.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.db.entity.Ad;
import ru.skypro.homework.db.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdRepository extends JpaRepository<Ad, Integer> {

    Optional<Ad> findById(Integer id);

    List<Ad> findAllByUser(User user);

    @Modifying
    @Query(value = "DELETE FROM ads a WHERE a.pk = :pk", nativeQuery = true)
    void delete(@Param("pk") Integer pk);

}
