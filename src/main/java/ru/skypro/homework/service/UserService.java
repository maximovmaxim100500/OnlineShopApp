package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.controller.dto.NewPassword;
import ru.skypro.homework.controller.dto.UpdateUser;
import ru.skypro.homework.controller.dto.UserDto;

import java.io.IOException;


public interface UserService {
    boolean setPassword(NewPassword newPasswordDto, String email);
    UserDto getUser(String email);

    UserDto updateUser(UpdateUser updateUser, String email);

    UserDto updateUserDto(UpdateUser updateUserDto, Long id);
    void updateAvatar(MultipartFile image, String email);
    byte[] getImage(String name) throws IOException;
}
