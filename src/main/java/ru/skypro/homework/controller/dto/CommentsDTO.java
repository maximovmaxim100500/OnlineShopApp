package ru.skypro.homework.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(name = "Comments")
public class CommentsDTO {

    private Integer count;
    private List<CommentDTO> results;
}
