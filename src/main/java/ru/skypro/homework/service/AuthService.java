package ru.skypro.homework.service;

import ru.skypro.homework.controller.dto.Register;

public interface AuthService {
    boolean register(Register registerDto);
    boolean authenticate(String username, String password);
}
