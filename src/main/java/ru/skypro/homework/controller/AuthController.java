package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.controller.dto.Login;
import ru.skypro.homework.controller.dto.Register;
import ru.skypro.homework.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login loginDto) {
        log.info("Авторизация для пользователя с именем " + loginDto.getUsername());
        boolean isAuthenticated = authService.authenticate(loginDto.getUsername(), loginDto.getPassword());
        if (isAuthenticated) {
            log.info("Успешная авторизация для пользователя с именем " + loginDto.getUsername());
            return ResponseEntity.ok().body("Successfully"); // Возвращает 200 OK при успешной аутентификации
        } else {
            log.info("Пользователь с именем " + loginDto.getUsername() + "не найден");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Возвращает 401 Unauthorized при ошибке аутентификации
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Register registerDto) {
        boolean isRegistered = authService.register(registerDto);
        if (isRegistered) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully"); // Возвращает 201 Created при успешной регистрации
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Возвращает 400 Bad Request при ошибке регистрации
        }
    }
}
