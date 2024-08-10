package ru.skypro.homework.service;

import ru.skypro.homework.controller.dto.RegisterDto;

public interface AuthService {
    boolean register(RegisterDto registerDto);
    boolean authenticate(String username, String password);
}
