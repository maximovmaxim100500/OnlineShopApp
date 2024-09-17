package ru.skypro.homework.config;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.db.entity.User;

import java.util.Collection;
import java.util.List;


/**
 * Класс AdsUserDetails реализует интерфейс UserDetails, предоставляя необходимые методы для интеграции
 * пользовательской сущности User с механизмами безопасности Spring Security.
 */
@AllArgsConstructor
public class AdsUserDetails implements UserDetails {

    // Пользователь, для которого предоставляются детали безопасности
    private User user;

    /**
     * Возвращает коллекцию прав (authorities) пользователя. В данном случае, правом является роль пользователя,
     * которая преобразуется в SimpleGrantedAuthority.
     *
     * @return коллекция, содержащая единственный элемент - SimpleGrantedAuthority с ролью пользователя
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRole().name());
        return List.of(grantedAuthority);
    }

    /**
     * Возвращает пароль пользователя.
     *
     * @return строка, представляющая пароль пользователя
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Возвращает имя пользователя, используемое для аутентификации. В данном случае это email.
     *
     * @return строка, представляющая email пользователя
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Указывает, что учетная запись пользователя не истекла.
     *
     * @return true, так как срок действия учетной записи не проверяется
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Указывает, что учетная запись пользователя не заблокирована.
     *
     * @return true, так как блокировка учетной записи не проверяется
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Указывает, что учетные данные пользователя (пароль) не истекли.
     *
     * @return true, так как срок действия учетных данных не проверяется
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Указывает, что учетная запись пользователя включена и может быть использована для аутентификации.
     *
     * @return true, так как статус включенности учетной записи не проверяется
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}
