package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.controller.dto.NewPasswordDTO;
import ru.skypro.homework.controller.dto.UpdateUserDTO;
import ru.skypro.homework.controller.dto.UserDTO;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.db.entity.User;
import ru.skypro.homework.service.UserAvatarService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserMapper userMapper;
    private final UserService userService;
    private final UserAvatarService userAvatarService;

    @Operation(
            tags = "Users",
            operationId = "setPassword",
            summary = "Password update",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User`s new password info",
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
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PostMapping("/set_password")
    public ResponseEntity setPassword(@RequestBody NewPasswordDTO newPassword, Authentication authentication) {
        log.info("New password: {}", newPassword);

        return userService.changeUserPassword(
                authentication.getName(),
                newPassword.getCurrentPassword(),
                newPassword.getNewPassword()
        );
    }

    @Operation(
            tags = "Users",
            operationId = "getUser",
            summary = "Getting information about an authorized user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDTO.class)
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
    public ResponseEntity<UserDTO> getUser(Authentication authentication) {
        User user = userService.findUserByEmail(authentication.getName());

        return ResponseEntity.ok(userMapper.userToUserDTO(user));
    }

    @Operation(
            tags = "Users",
            operationId = "updateUser",
            summary = "Updating information about an authorized user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User`s new info",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UpdateUserDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PatchMapping("/me")
    public ResponseEntity<UpdateUserDTO> updateUser(
            @RequestBody UpdateUserDTO updateUser,
            Authentication authentication
    ) {
        User user = userMapper.updateUserDTOToUser(updateUser);
        user.setEmail(authentication.getName());

        userService.updateUserInfo(user);

        return ResponseEntity.ok().build();
    }

    @Operation(
            tags = "Users",
            operationId = "updateUserImage",
            summary = "Update the avatar of an authorized user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User`s new avatar",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
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
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity updateUserImage(
            @RequestParam MultipartFile image,
            Authentication authentication
    ) throws IOException {
        userAvatarService.saveUserAvatar(authentication.getName(), image);

        return ResponseEntity.ok().build();
    }

}
