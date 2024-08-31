package ru.skypro.homework.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.db.entity.Comment;
import ru.skypro.homework.db.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByAdId(Long id);
    List<Comment> findAllByUser(User user);

    void deleteByAdIdAndId(Long adId, Long id);

    Optional<Comment> findCommentByAdIdAndId(Long adId,Long id);

    void deleteAllByAdId(Long id);
}
