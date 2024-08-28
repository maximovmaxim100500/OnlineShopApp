package ru.skypro.homework.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdDto {
    private Long author; // id автора
    private String image;
    private Long pk; // id объявления
    private Integer price;
    private String title;
}
