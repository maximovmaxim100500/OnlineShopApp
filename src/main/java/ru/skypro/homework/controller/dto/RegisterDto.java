package ru.skypro.homework.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skypro.homework.controller.dto.enums.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDto {
    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
}
