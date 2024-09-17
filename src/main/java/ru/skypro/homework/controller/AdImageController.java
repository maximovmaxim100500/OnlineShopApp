package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.service.AdImageService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Контроллер AdImageController предоставляет API для работы с изображениями объявлений.
 * Реализует метод для получения изображения объявления по его идентификатору.
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@PreAuthorize("permitAll()")
@RequestMapping("/images")
public class AdImageController {

    private final AdImageService adImageService;

    /**
     * Метод для получения изображения объявления по его идентификатору.
     * Изображение возвращается в виде байтового потока через HttpServletResponse.
     *
     * @param id       идентификатор объявления, изображение которого нужно получить
     * @param response HttpServletResponse, в который записывается изображение
     * @throws IOException если возникнет ошибка при записи изображения в поток
     */
    @Operation(
            tags = "Images",
            operationId = "getAdImage",
            summary = "Get ad image",
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
                            responseCode = "404",
                            description = "Not found",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping("/{id}")
    public void getAdImage(
            @Parameter(name = "id", description = "Ad identifier") @PathVariable(name = "id") Integer id,
            HttpServletResponse response
    ) throws IOException {
        adImageService.getAdImage(id, response);
    }

}
