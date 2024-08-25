package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.controller.dto.Register;
import ru.skypro.homework.db.entity.User;
import ru.skypro.homework.db.repository.UserRepository;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.service.AuthService;

import java.util.Optional;

import static ru.skypro.homework.controller.dto.enums.Role.USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public boolean authenticate(String username, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(username);
        if (optionalUser.isEmpty()) {
            log.debug("User not found");
            return false;
        }
        return passwordEncoder.matches(password, optionalUser.get().getPassword());
    }

    @Override
    public boolean register(Register register) {
        if (userRepository.findByEmail(register.getUsername().toLowerCase()).isPresent()) {
            return false;
        }
        User user = userMapper.toUser(register);
        user.setEmail(user.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(register.getRole() == null ? USER : register.getRole());
        userRepository.save(user);
        log.debug("Registered a new user");
        return true;
    }
}
