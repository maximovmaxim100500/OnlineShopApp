package ru.skypro.homework.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "Ad")
public class AdDTO {

    private Integer author;
    private String image;
    private Integer pk;
    private Integer price;
    private String title;
    private String description;

}
