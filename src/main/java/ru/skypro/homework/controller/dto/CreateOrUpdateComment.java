package ru.skypro.homework.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateComment {
    @Size(min = 8, max = 64)
    private String text;
}
