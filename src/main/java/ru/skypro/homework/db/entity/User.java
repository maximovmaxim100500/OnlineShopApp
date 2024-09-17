package ru.skypro.homework.db.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.skypro.homework.controller.dto.enums.Role;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;             // Уникальный идентификатор пользователя,
    @Column(unique = true)
    private String email;           // Электронная почта пользователя (уникальное поле)
    private String password;        // Пароль пользователя
    private String firstName;       // Имя пользователя
    private String lastName;        // Фамилия пользователя
    private String phone;           // Телефон пользователя
    @Enumerated(EnumType.STRING)
    private Role role;              // Роль пользователя (например, ADMIN, USER)
    private String image;           // Путь к изображению профиля пользователя

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }

}
