package ru.skypro.homework.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.controller.dto.MyUserDetailsDto;
import ru.skypro.homework.db.repository.UserRepository;
import ru.skypro.homework.exception.UserWithEmailNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
/**
 * Сервис, реализующий интерфейс UserDetailsService для загрузки информации о пользователе по его email.
 * Этот сервис используется Spring Security для аутентификации.
 */
@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository; // Репозиторий для доступа к данным пользователей
    private final MyUserDetails myUserDetails;   // Класс для хранения деталей пользователя в контексте текущего запроса
    private final UserMapper userMapper;         // Mapper для преобразования User в MyUserDetailsDto

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Находит пользователя по email и преобразует его в MyUserDetailsDto
        MyUserDetailsDto myUserDetailsDto = userRepository.findByEmail(email)
                .map(user -> userMapper.toMyUserDetailsDto(user))
                .orElseThrow(() -> new UserWithEmailNotFoundException(email)); // Если пользователь не найден, выбрасывается исключение
        // Устанавливает найденные данные пользователя в myUserDetails
        myUserDetails.setMyUserDetailsDto(myUserDetailsDto);
        return myUserDetails; // Возвращает объект, реализующий UserDetails, используемый Spring Security
    }
}

