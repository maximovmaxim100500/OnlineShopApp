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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.controller.dto.*;
import ru.skypro.homework.db.entity.Comment;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CommentService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер `AdsController` предоставляет API для управления объявлениями и комментариями к ним.
 * Включает методы для получения, добавления, обновления и удаления объявлений и комментариев,
 * а также для работы с изображениями объявлений.
 *
 * Контроллер реализует аутентификацию и авторизацию, используя Spring Security.
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/ads")
public class AdsController {

    private final AdsService adsService;
    private final CommentService commentService;
    private final AdMapper adMapper;
    private final CommentMapper commentMapper;

    /**
     * Возвращает все объявления.
     *
     * Этот метод доступен для всех пользователей, включая неавторизованных.
     * Возвращает список всех объявлений с информацией о количестве объявлений.
     *
     * @return объект `ResponseEntity<AdsDTO>`, содержащий список объявлений и количество объявлений.
     */
    @Operation(
            tags = "Ads",
            operationId = "getAllAds",
            summary = "Receive all ads",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AdsDTO.class)
                    )
            )
    )
    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<AdsDTO> getAllAds() {
        List<AdDTO> ads = adsService.getAllAds().stream()
                .map(adMapper::adToAdDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                AdsDTO.builder()
                        .count(ads.size())
                        .results(ads)
                        .build()
        );
    }

    /**
     * Добавляет новое объявление.
     *
     * Этот метод требует аутентификации и позволяет пользователю добавить новое объявление,
     * загрузив изображение и указав свойства объявления.
     *
     * @param image          изображение объявления, загружаемое пользователем.
     * @param ad             свойства объявления, которые пользователь хочет создать.
     * @param authentication объект аутентификации, содержащий информацию о текущем пользователе.
     * @return объект `ResponseEntity<AdDTO>`, содержащий данные добавленного объявления.
     * @throws IOException если возникает ошибка при обработке загруженного изображения.
     */
    @Operation(
            tags = "Ads",
            operationId = "addAd",
            summary = "Adding an ad",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdDTO> addAd(
            @RequestPart MultipartFile image,
            @RequestPart(name = "properties") CreateOrUpdateAdDTO ad,
            Authentication authentication
    ) throws IOException {
        return adsService.addAd(ad, authentication.getName(), image);
    }

    /**
     * Возвращает все комментарии к указанному объявлению.
     *
     * Этот метод доступен только авторизованным пользователям.
     * Возвращает список всех комментариев к указанному объявлению и количество комментариев.
     *
     * @param id идентификатор объявления, к которому нужно получить комментарии.
     * @return объект `ResponseEntity<CommentsDTO>`, содержащий список комментариев и количество комментариев.
     */
    @Operation(
            tags = "Comments",
            operationId = "getComments",
            summary = "Receiving comments on the ad",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentsDTO.class)
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
    @GetMapping("/{id}/comments")
    public ResponseEntity<CommentsDTO> getComments(
            @Parameter(name = "id", description = "identifier of ad") @PathVariable Integer id
    ) {
        List<CommentDTO> adCommentsDTO = commentService.getAllCommentsByAdId(id).stream()
                .map(commentMapper::commentToCommentDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CommentsDTO.builder()
                .count(adCommentsDTO.size())
                .results(adCommentsDTO)
                .build());
    }

    /**
     * Добавляет новый комментарий к указанному объявлению.
     *
     * Этот метод требует аутентификации и позволяет пользователю добавить комментарий к объявлению.
     *
     * @param id             идентификатор объявления, к которому добавляется комментарий.
     * @param comment        объект DTO с данными комментария.
     * @param authentication объект аутентификации, содержащий информацию о текущем пользователе.
     * @return объект `ResponseEntity<CommentDTO>`, содержащий данные добавленного комментария.
     */
    @Operation(
            tags = "Comments",
            operationId = "addComment",
            summary = "Adding a comment to an ad",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentDTO.class)
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
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDTO> addComment(
            @Parameter(name = "id", description = "Identifier of ad") @PathVariable Integer id,
            @RequestBody CreateOrUpdateCommentDTO comment,
            Authentication authentication
    ) {
        Comment addedComment = commentService.addCommentToAdByItsId(id, authentication.getName(), comment);

        return ResponseEntity.ok(commentMapper.commentToCommentDTO(addedComment));
    }

    /**
     * Возвращает подробную информацию об указанном объявлении.
     *
     * Этот метод доступен только авторизованным пользователям.
     * Возвращает расширенную информацию о объявлении.
     *
     * @param id идентификатор объявления, информацию о котором нужно получить.
     * @return объект `ResponseEntity<ExtendedAdDTO>`, содержащий данные объявления.
     */
    @Operation(
            tags = "Ads",
            operationId = "getAds",
            summary = "Getting information about the ad",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExtendedAdDTO.class)
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
    public ResponseEntity<ExtendedAdDTO> getAds(
            @Parameter(name = "id", description = "Identifier of ad") @PathVariable Integer id
    ) {
        return adsService.getExtendedAdInfo(id);
    }

    /**
     * Удаляет указанное объявление.
     *
     * Этот метод требует аутентификации и позволяет пользователю удалить своё объявление.
     * Если объявление не найдено или пользователь не имеет права на его удаление, возвращаются соответствующие ошибки.
     *
     * @param id             идентификатор объявления, которое нужно удалить.
     * @param authentication объект аутентификации, содержащий информацию о текущем пользователе.
     * @return объект `ResponseEntity<?>`, указывающий на успешное удаление или содержащий статус ошибки.
     */
    @Operation(
            tags = "Ads",
            operationId = "removeAd",
            summary = "Deleting an ad",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "No Content",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeAd(
            @Parameter(name = "id", description = "Identifier of ad") @PathVariable Integer id,
            Authentication authentication
    ) {
        return ResponseEntity.status(adsService.deleteAdById(id, authentication)).build();
    }

    /**
     * Обновляет информацию об указанном объявлении.
     *
     * Этот метод требует аутентификации и позволяет пользователю обновить данные своего объявления.
     * Если объявление не найдено или пользователь не имеет права на его обновление, возвращаются соответствующие ошибки.
     *
     * @param id             идентификатор объявления, которое нужно обновить.
     * @param ad             объект DTO с новыми данными объявления.
     * @param authentication объект аутентификации, содержащий информацию о текущем пользователе.
     * @return объект `ResponseEntity<AdDTO>`, содержащий обновленные данные объявления.
     */
    @Operation(
            tags = "Ads",
            operationId = "updateAds",
            summary = "Updating ad information",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<AdDTO> updateAds(
            @Parameter(name = "id", description = "Identifier of ad") @PathVariable Integer id,
            @RequestBody CreateOrUpdateAdDTO ad,
            Authentication authentication
    ) {
        return adsService.updateAdById(id, ad, authentication.getName());
    }

    /**
     * Удаляет комментарий, связанный с объявлением.
     * Этот метод позволяет аутентифицированному пользователю удалить комментарий, если он является его автором
     * или имеет соответствующие права.
     *
     * @param adId       идентификатор объявления, к которому относится комментарий
     * @param commentId  идентификатор комментария, который нужно удалить
     * @param authentication информация об аутентификации текущего пользователя
     * @return ResponseEntity с соответствующим HTTP-статусом:
     *         - 200 (OK), если комментарий успешно удален
     *         - 401 (Unauthorized), если пользователь не аутентифицирован
     *         - 403 (Forbidden), если пользователь не имеет прав на удаление этого комментария
     *         - 404 (Not Found), если комментарий или объявление не найдены
     */
    @Operation(
            tags = "Comments",
            operationId = "deleteComment",
            summary = "Deleting a comment",
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
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(
            @Parameter(name = "adId", description = "Identifier of ad") @PathVariable Integer adId,
            @Parameter(name = "commentId", description = "Identifier of comment") @PathVariable Integer commentId,
            Authentication authentication
    ) {
        return ResponseEntity.status(
                commentService.deleteAdCommentByItsId(adId, commentId, authentication.getName())
        ).build();
    }

    /**
     * Обновляет комментарий, связанный с объявлением.
     * Этот метод позволяет аутентифицированному пользователю редактировать свой комментарий.
     *
     * @param adId          идентификатор объявления, к которому относится комментарий
     * @param commentId     идентификатор комментария, который нужно обновить
     * @param comment       новый контент комментария в виде DTO
     * @param authentication информация об аутентификации текущего пользователя
     * @return ResponseEntity с обновленным комментарием в теле ответа:
     *         - 200 (OK), если комментарий успешно обновлен
     *         - 401 (Unauthorized), если пользователь не аутентифицирован
     *         - 403 (Forbidden), если пользователь не имеет прав на обновление этого комментария
     *         - 404 (Not Found), если комментарий или объявление не найдены
     */
    @Operation(
            tags = "Comments",
            operationId = "updateComment",
            summary = "Updating a comment",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @Parameter(name = "adId", description = "Identifier of ad") @PathVariable Integer adId,
            @Parameter(name = "commentId", description = "Identifier of comment") @PathVariable Integer commentId,
            @RequestBody CreateOrUpdateCommentDTO comment,
            Authentication authentication
    ) {
        return commentService.updateAdCommentByItsId(adId, commentId, comment, authentication.getName());
    }

    /**
     * Получает список объявлений, созданных аутентифицированным пользователем.
     *
     * @param authentication информация об аутентификации текущего пользователя
     * @return ResponseEntity с DTO, содержащим список объявлений и их количество:
     *         - 200 (OK), если запрос успешен
     *         - 401 (Unauthorized), если пользователь не аутентифицирован
     */
    @Operation(
            tags = "Ads",
            operationId = "getAdsMe",
            summary = "Receiving authenticated user`s ads",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdsDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping("/me")
    public ResponseEntity<AdsDTO> getAdsMe(Authentication authentication) {
        List<AdDTO> ads = adsService.getUsersAds(authentication.getName()).stream()
                .map(adMapper::adToAdDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                AdsDTO.builder()
                        .count(ads.size())
                        .results(ads)
                        .build()
        );
    }

    /**
     * Обновляет изображение, связанное с объявлением.
     * Этот метод позволяет аутентифицированному пользователю изменить изображение объявления.
     *
     * @param id            идентификатор объявления, изображение которого нужно обновить
     * @param image         новое изображение в виде файла
     * @param authentication информация об аутентификации текущего пользователя
     * @return ResponseEntity с массивом байтов, представляющих обновленное изображение:
     *         - 200 (OK), если изображение успешно обновлено
     *         - 401 (Unauthorized), если пользователь не аутентифицирован
     *         - 403 (Forbidden), если пользователь не имеет прав на обновление этого изображения
     *         - 404 (Not Found), если объявление не найдено
     * @throws IOException если возникли проблемы с обработкой файла изображения
     */
    @Operation(
            tags = "Ads",
            operationId = "updateImage",
            summary = "Updating the picture of ad",
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
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> updateAdImage(
            @Parameter(name = "id", description = "Identifier of ad") @PathVariable Integer id,
            @RequestParam MultipartFile image,
            Authentication authentication
    ) throws IOException {
        return adsService.updateAdImageById(id, image, authentication.getName());
    }

}