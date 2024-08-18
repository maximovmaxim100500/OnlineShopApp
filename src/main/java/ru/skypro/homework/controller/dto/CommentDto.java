package ru.skypro.homework.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private Integer author;             // id автора комментария
    private String authorImage;         // ссылка на аватар автора комментария
    private String authorFirstName;     // имя создателя комментария
    private Integer createdAt;             // дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970
    private Integer pk;                 // id комментария
    private String text;                // текст комментария
}
