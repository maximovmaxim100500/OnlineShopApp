package ru.skypro.homework.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.controller.dto.AdDTO;
import ru.skypro.homework.controller.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.controller.dto.ExtendedAdDTO;
import ru.skypro.homework.db.entity.Ad;

import java.io.IOException;
import java.util.List;

public interface AdsService {

    ResponseEntity<AdDTO> addAd(CreateOrUpdateAdDTO ad, String username, MultipartFile image) throws IOException;

    List<Ad> getAllAds();

    List<Ad> getUsersAds(String username);

    ResponseEntity<ExtendedAdDTO> getExtendedAdInfo(Integer id);

    Ad getAdById(Integer id);

    HttpStatus deleteAdById(Integer id, Authentication authentication);

    ResponseEntity<AdDTO> updateAdById(Integer id, CreateOrUpdateAdDTO ad, String username);

    ResponseEntity<byte[]> updateAdImageById(Integer id, MultipartFile image, String username) throws IOException;

}
