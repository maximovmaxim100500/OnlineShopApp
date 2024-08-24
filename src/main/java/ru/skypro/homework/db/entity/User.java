package ru.skypro.homework.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import ru.skypro.homework.controller.dto.enums.Role;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String password;

    @Column(name = "firstname",nullable = false)
    @Size(min = 3, max = 10)
    private String firstName;

    @Column(name = "lastname",nullable = false)
    @Size(min = 3, max = 10)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private String image;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ad> ads;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // getAuthorities() — возвращает права пользователя в виде коллекции
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email; // возвращает email как имя пользователя
    }

    // Метод возвращает, истек ли срок действия аккаунта. Всегда возвращает true, что означает, что аккаунт не истек.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Метод возвращает, заблокирован ли аккаунт. Всегда возвращает true, что означает, что аккаунт не заблокирован.
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Метод возвращает, истек ли срок действия учетных данных. Всегда возвращает true, что означает,
    // что учетные данные не истекли.
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Метод возвращает, активен ли аккаунт. Всегда возвращает true, что означает, что аккаунт активен.
    @Override
    public boolean isEnabled() {
        return true;
    }
}
