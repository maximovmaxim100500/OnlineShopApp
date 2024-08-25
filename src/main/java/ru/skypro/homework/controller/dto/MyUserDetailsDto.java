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
public class MyUserDetailsDto {
    private Integer id;
    private String email;
    private String password;
    private Role role;
}
