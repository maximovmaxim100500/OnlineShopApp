package ru.skypro.homework.service;

import ru.skypro.homework.controller.dto.AdDto;

import java.util.List;

public interface AdService {
    AdDto createAd(AdDto adDto);
    AdDto getAdById(Long id);
    List<AdDto> getAllAds();
    AdDto updateAd(Long id, AdDto adDto);
    void deleteAd(Long id);
}
