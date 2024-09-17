package ru.skypro.homework.db.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ads")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;                 // Уникальный идентификатор объявления
    private Integer price;              // Цена объявления
    private String title;               // Заголовок объявления
    private String description;         // Описание объявления
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;                  // Пользователь, который создал это объявление

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ad ad = (Ad) o;
        return Objects.equals(price, ad.price)
                && Objects.equals(title, ad.title)
                && Objects.equals(description, ad.description)
                && Objects.equals(user, ad.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, title, description, user);
    }
}
