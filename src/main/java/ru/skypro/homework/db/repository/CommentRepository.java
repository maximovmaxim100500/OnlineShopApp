package ru.skypro.homework.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.db.entity.Ad;
import ru.skypro.homework.db.entity.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByAd(Ad ad);

    Optional<Comment> findByPk(Integer id);

    @Modifying
    @Query(value = "DELETE FROM comments c WHERE c.pk = :pk", nativeQuery = true)
    void delete(@Param("pk") Integer pk);
}
