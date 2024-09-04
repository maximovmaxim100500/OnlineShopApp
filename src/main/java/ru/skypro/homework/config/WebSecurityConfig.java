package ru.skypro.homework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Конфигурационный класс WebSecurityConfig отвечает за настройку безопасности в приложении.
 * Включает в себя настройку аутентификации, авторизации и защиты ресурсов.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

    @Autowired
    private AdsUserDetailsService userDetailsService;
    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login",
            "/register"
    };

    /**
     * Настраивает цепочку фильтров безопасности. Отключает защиту CSRF, разрешает доступ к определённым
     * маршрутам без аутентификации, и включает базовую HTTP-аутентификацию.
     *
     * @param http объект HttpSecurity для конфигурации безопасности HTTP
     * @return настроенный объект SecurityFilterChain
     * @throws Exception если возникнет ошибка конфигурации
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests(
                        authorization ->
                                authorization
                                        .mvcMatchers(AUTH_WHITELIST)
                                        .permitAll()
                                        .mvcMatchers("/users/**", "/avatars/**")
                                        .authenticated())
                .cors()
                .and()
                .httpBasic(withDefaults());
        return http.build();
    }

    /**
     * Создаёт и настраивает бин PasswordEncoder для шифрования паролей с использованием BCrypt.
     *
     * @return экземпляр BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Настраивает провайдер аутентификации с использованием DAO (Data Access Object) для загрузки
     * пользовательских данных и проверки паролей.
     *
     * @return настроенный DaoAuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder()); // Устанавливает шифратор паролей
        provider.setUserDetailsService(userDetailsService); // Устанавливает сервис загрузки пользовательских данных

        return provider;
    }
}
