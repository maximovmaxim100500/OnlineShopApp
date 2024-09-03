package ru.skypro.homework.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface UserAvatarService {

    ResponseEntity<byte[]> getUserAvatar(Integer id);

    void saveUserAvatar(String username, MultipartFile image) throws IOException;

}
