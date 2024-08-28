package ru.skypro.homework.service;

import ru.skypro.homework.controller.dto.AdDto;
import ru.skypro.homework.controller.dto.AdsDto;
import ru.skypro.homework.controller.dto.CreateOrUpdateAd;

import javax.transaction.Transactional;

public interface AdService {
    AdDto createAd(CreateOrUpdateAd createAds, String email);
    AdsDto getAllAds();
    AdsDto getMyAds(String email);
    AdDto updateAd(CreateOrUpdateAd createOrUpdateAd, Long id);
    @Transactional
    void removeAd(Long id);
}
