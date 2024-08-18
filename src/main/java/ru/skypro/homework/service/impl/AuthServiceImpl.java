package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.controller.dto.Register;
import ru.skypro.homework.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean authenticate(String username, String password) {
        if (!userDetailsManager.userExists(username)) {
            return false;
        }
        UserDetails userDetails = userDetailsManager.loadUserByUsername(username);
        return passwordEncoder.matches(password, userDetails.getPassword());
    }

    @Override
    public boolean register(Register registerDto) {
        if (userDetailsManager.userExists(registerDto.getUsername())) {
            return false;
        }
        UserDetails userDetails = User.builder()
                .username(registerDto.getUsername())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .roles(registerDto.getRole().name()) // Используйте name() для получения строки роли
                .build();
        userDetailsManager.createUser(userDetails);
        return true;
    }
}
