package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.controller.dto.NewPassword;
import ru.skypro.homework.controller.dto.UpdateUser;
import ru.skypro.homework.controller.dto.UserDto;
import ru.skypro.homework.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/set_password")
    public ResponseEntity<NewPassword> setPassword(@RequestBody NewPassword newPasswordDto,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        userService.setPassword(newPasswordDto, userDetails.getUsername());
        return ResponseEntity.ok(newPasswordDto);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getUser(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getUser(userDetails.getUsername()));
    }

    @PatchMapping("/me")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.updateUser(userDto, userDetails.getUsername()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UpdateUser updateUserDto,
                                              @PathVariable Long id) {
        return ResponseEntity.ok(userService.updateUserDto(updateUserDto, id));
    }
}
