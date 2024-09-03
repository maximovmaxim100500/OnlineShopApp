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