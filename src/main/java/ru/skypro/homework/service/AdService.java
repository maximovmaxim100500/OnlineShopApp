package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.controller.dto.AdDto;
import ru.skypro.homework.controller.dto.AdsDto;
import ru.skypro.homework.controller.dto.CreateOrUpdateAd;
import ru.skypro.homework.controller.dto.ExtendedAd;

import javax.transaction.Transactional;
import java.io.IOException;

public interface AdService {
    AdDto createAd(CreateOrUpdateAd createAds, String email);
    AdsDto getAllAds();
    ExtendedAd getAds(Long id);
    AdsDto getMyAds(String email);
    AdDto updateAd(CreateOrUpdateAd createOrUpdateAd, Long id);
    @Transactional
    void removeAd(Long id);
    void updateAdsImage(Long id, MultipartFile image);

    byte[] getImage(String name) throws IOException;
}
