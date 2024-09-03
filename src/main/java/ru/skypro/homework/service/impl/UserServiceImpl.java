package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.db.entity.User;
import ru.skypro.homework.db.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User by username " + email + " not found."));
    }

    @Override
    public void updateUserInfo(User user) {
        User savedUser = findUserByEmail(user.getEmail());

        savedUser.setFirstName(user.getFirstName());
        savedUser.setLastName(user.getLastName());
        savedUser.setPhone(user.getPhone());

        userRepository.save(savedUser);
    }

    @Override
    public ResponseEntity changeUserPassword(String username, String currentPassword, String newPassword) {
        Optional<User> user = userRepository.findByEmail(username);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User currentUser = user.get();

        if (!passwordEncoder.matches(currentPassword, user.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            currentUser.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(currentUser);
        }

        return ResponseEntity.ok().build();
    }

}
