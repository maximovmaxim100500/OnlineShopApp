package ru.skypro.homework.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.db.entity.User;
import ru.skypro.homework.db.entity.UserAvatar;

import java.util.Optional;

@Repository
public interface UserAvatarRepository extends JpaRepository<UserAvatar, Integer> {

    Optional<UserAvatar> findById(Integer id);

    Optional<UserAvatar> findByUser(User user);

}
