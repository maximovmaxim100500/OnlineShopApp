package ru.skypro.homework.service;

import org.springframework.http.ResponseEntity;
import ru.skypro.homework.db.entity.User;

public interface UserService {

    User saveUser(User user);

    User findUserByEmail(String email);

    void updateUserInfo(User user);

    ResponseEntity changeUserPassword(String username, String currentPassword, String newPassword);

}
