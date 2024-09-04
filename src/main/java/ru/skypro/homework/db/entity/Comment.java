package ru.skypro.homework.db.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;                     // Уникальный идентификатор комментария
    @Column(name = "created_at")
    private Date createdAt;                 // Дата и время создания комментария
    private String text;                    // Текст комментария
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;                      // Пользователь, оставивший комментарий
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id")
    private Ad ad;                          // Объявление, к которому относится этот комментарий

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(text, comment.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(text);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "pk=" + pk +
                ", createdAt=" + createdAt +
                ", text='" + text + '\'' +
                ", user=" + user +
                ", ad=" + ad +
                '}';
    }

}
