package ru.skypro.homework.service;

import ru.skypro.homework.controller.dto.NewPassword;
import ru.skypro.homework.controller.dto.UpdateUser;
import ru.skypro.homework.controller.dto.UserDto;


public interface UserService {
    boolean setPassword(NewPassword newPasswordDto, String email);
    UserDto getUser(String email);

    UserDto updateUser(UserDto userDto, String email);

    UserDto updateUserDto(UpdateUser updateUserDto, Long id);
}
