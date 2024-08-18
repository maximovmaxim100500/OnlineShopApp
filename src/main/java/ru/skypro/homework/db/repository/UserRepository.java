package ru.skypro.homework.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.db.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
