package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.controller.dto.RegisterDTO;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;
    private final UserService userService;

    @Override
    public boolean login(String userName, String password) {
        if (!manager.userExists(userName)) {
            return false;
        }
        UserDetails userDetails = manager.loadUserByUsername(userName);
        return encoder.matches(password, userDetails.getPassword());
    }

    @Override
    @Transactional
    public boolean register(RegisterDTO register) {
        if (manager.userExists(register.getUserName())) {
            return false;
        }

        manager.createUser(
                User.builder()
                        .passwordEncoder(this.encoder::encode)
                        .password(register.getPassword())
                        .username(register.getUserName())
                        .roles(register.getRole().name())
                        .build());

        ru.skypro.homework.db.entity.User createdUser = userService.findUserByEmail(register.getUserName());

        createdUser.setFirstName(register.getFirstName());
        createdUser.setLastName(register.getLastName());
        createdUser.setPhone(register.getPhone());

        userService.saveUser(createdUser);

        return true;
    }

}
