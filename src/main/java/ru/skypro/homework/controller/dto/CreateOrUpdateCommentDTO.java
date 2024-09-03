package ru.skypro.homework.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Schema(name = "CreateOrUpdateComment")
public class CreateOrUpdateCommentDTO {

    @NotBlank
    @Size(min = 8, max = 64)
    private String text;
}
