package ru.skypro.homework.security;

import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import ru.skypro.homework.controller.dto.MyUserDetailsDto;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * Реализация интерфейса UserDetails для Spring Security, обеспечивающая детали аутентификации и авторизации пользователя.
 * Класс аннотирован как @RequestScope, что означает создание нового экземпляра для каждого HTTP-запроса.
 */
@Component
@RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyUserDetails implements UserDetails {

    private MyUserDetailsDto myUserDetailsDto; // DTO, содержащий данные пользователя

    // Метод для установки DTO данных пользователя
    public void setMyUserDetailsDto(MyUserDetailsDto myUserDetailsDto) {
        this.myUserDetailsDto = myUserDetailsDto;
    }

    // Возвращает коллекцию прав (ролей) пользователя
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Optional.ofNullable(myUserDetailsDto)
                .map(MyUserDetailsDto::getRole)
                .map(role -> "ROLE_" + role) // Добавляет префикс "ROLE_" к роли пользователя
                .map(SimpleGrantedAuthority::new) // Создает объект SimpleGrantedAuthority
                .map(Collections::singleton) // Оборачивает в одиночную коллекцию
                .orElse(Collections.emptySet()); // Возвращает пустую коллекцию, если роль не найдена
    }

    // Возвращает пароль пользователя
    @Override
    public String getPassword() {
        return Optional.ofNullable(myUserDetailsDto)
                .map(MyUserDetailsDto::getPassword)
                .orElse(null);
    }

    // Возвращает имя пользователя (email)
    @Override
    public String getUsername() {
        return Optional.ofNullable(myUserDetailsDto)
                .map(MyUserDetailsDto::getEmail)
                .orElse(null);
    }

    // Возвращает true, если учетная запись пользователя не просрочена
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Возвращает true, если учетная запись пользователя не заблокирована
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Возвращает true, если учетные данные пользователя не просрочены
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Возвращает true, если учетная запись включена
    @Override
    public boolean isEnabled() {
        return true; // Можно добавить логику проверки включения аккаунта
    }
}
