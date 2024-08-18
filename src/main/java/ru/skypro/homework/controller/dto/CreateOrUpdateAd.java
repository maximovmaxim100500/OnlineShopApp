package ru.skypro.homework.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrUpdateAd {
    @Size(min = 8, max = 64)
    private String description;
    @Size(min = 0, max = 10000000)
    private Integer price;
    @Size(min = 4, max = 32)
    private String title;
}
