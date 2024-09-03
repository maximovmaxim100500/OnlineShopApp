package ru.skypro.homework.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skypro.homework.controller.dto.enums.Role;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Schema(name = "Register")
public class RegisterDTO {

    @JsonProperty("username")
    @Size(min = 4, max = 32)
    private String userName;
    @Size(min = 8, max = 16)
    private String password;
    @Size(min = 2, max = 16)
    private String firstName;
    @Size(min = 2, max = 16)
    private String lastName;
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;
    private Role role;
}
