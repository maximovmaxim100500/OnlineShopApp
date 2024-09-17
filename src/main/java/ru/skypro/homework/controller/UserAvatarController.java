package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.service.UserAvatarService;

/**
 * Контроллер для работы с аватарами пользователей.
 * <p>
 * Этот контроллер предоставляет функционал для получения аватара пользователя по его идентификатору.
 * Все методы контроллера требуют авторизации. Аватар возвращается в виде байтового массива.
 * </p>
 */
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RestController
@RequestMapping("/avatars")
public class UserAvatarController {

    private final UserAvatarService userAvatarService;

    /**
     * Получение аватара пользователя.
     * Этот метод позволяет получить аватар пользователя по его идентификатору.
     *
     * @param id идентификатор аватара пользователя
     * @return ResponseEntity:
     *         - 200 (OK), если аватар успешно найден и возвращен
     *         - 401 (Unauthorized), если запрос не авторизован
     *         - 404 (Not Found), если аватар не найден
     */
    @Operation(
            tags = "UserAvatar",
            operationId = "getUserAvatar",
            summary = "Get user avatar",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = byte.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<byte[]> getUserAvatar(
            @Parameter(name = "id", description = "User avatar identifier")
            @PathVariable(name = "id") Integer id) {
        return userAvatarService.getUserAvatar(id);
    }

}
