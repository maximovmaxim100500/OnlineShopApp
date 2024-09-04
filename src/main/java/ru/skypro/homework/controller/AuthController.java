package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.controller.dto.LoginDTO;
import ru.skypro.homework.controller.dto.RegisterDTO;
import ru.skypro.homework.service.AuthService;

/**
 * Контроллер для авторизации и регистрации пользователей.
 * <p>
 * Этот контроллер управляет процессом авторизации и регистрации пользователей в системе.
 * Он предоставляет методы для входа в систему и создания новых учетных записей.
 * Все методы используют JSON для обмена данными с клиентом и возвращают соответствующие HTTP статусы.
 * </p>
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Авторизация пользователя.
     * Этот метод позволяет пользователю войти в систему, предоставив имя пользователя и пароль.
     *
     * @param login информация для входа в систему
     * @return ResponseEntity:
     *         - 200 (OK), если аутентификация прошла успешно
     *         - 401 (Unauthorized), если учетные данные неверны
     */
    @Operation(tags = "Authorization",
            operationId = "login",
            summary = "User Authorization",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User login info",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO login) {
        if (authService.login(login.getUserName(), login.getPassword())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Регистрация нового пользователя.
     * Этот метод позволяет зарегистрировать нового пользователя, предоставив его данные.
     *
     * @param register информация о новом пользователе
     * @return ResponseEntity:
     *         - 201 (Created), если регистрация прошла успешно
     *         - 400 (Bad Request), если данные неверны или отсутствуют
     */
    @Operation(
            tags = "Registration",
            operationId = "register",
            summary = "User registration",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "New user info",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO register) {
        if (authService.register(register)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
