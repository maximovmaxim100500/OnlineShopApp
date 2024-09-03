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
    private Integer pk;
    private Integer price;
    private String title;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

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
