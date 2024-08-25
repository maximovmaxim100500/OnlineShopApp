package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.controller.dto.UserDto;
import ru.skypro.homework.db.entity.User;
import ru.skypro.homework.db.repository.UserRepository;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    // Разрешить доступ только владельцу записи
    @PreAuthorize("#userId == authentication.principal.id")
    @Override
    public UserDto getUserById(Long id) {
        return userMapper.toDto(userRepository.findById(id).orElse(null));
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateEntityFromDto(userDto, user);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    // Только пользователи с ролью ADMIN могут выполнить этот метод или сам владелец записи
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
