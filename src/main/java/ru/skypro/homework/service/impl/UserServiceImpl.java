package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.controller.dto.NewPassword;
import ru.skypro.homework.controller.dto.UpdateUser;
import ru.skypro.homework.controller.dto.UserDto;
import ru.skypro.homework.db.entity.User;
import ru.skypro.homework.db.repository.UserRepository;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.exception.UserWithEmailNotFoundException;
import ru.skypro.homework.exception.UserWithIdNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    @Override
    public boolean setPassword(NewPassword newPasswordDto, String email) {
        if (newPasswordDto.getCurrentPassword() == null || newPasswordDto.getNewPassword() == null) {
            log.warn("Current password or new password is null");
            return false;
        }
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(newPasswordDto.getCurrentPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPasswordDto.getNewPassword()));
                userRepository.save(user);
                log.info("Пароль обновлен");
                return true;
            }
        }
       log.info("Не удалось обновить пароль");
        return false;
    }

    @Override
    public UserDto getUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserWithEmailNotFoundException(email));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        userMapper.updateEntityFromDto(userDto, user);
        userRepository.save(user);
        log.trace("User updated");
        return userMapper.toDto(user);
    }

    @Override
    public UserDto updateUserDto(UpdateUser updateUserDto, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserWithIdNotFoundException("Update user with id:", id));

        userMapper.updateUser(updateUserDto, user);
        userRepository.save(user);
        log.info("Обновлен пользователь с id: " + id);
        return userMapper.toDto(user);
    }
}
