package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.controller.dto.AdDto;
import ru.skypro.homework.controller.dto.AdsDto;
import ru.skypro.homework.controller.dto.CreateOrUpdateAd;
import ru.skypro.homework.controller.dto.ExtendedAd;
import ru.skypro.homework.db.entity.Ad;
import ru.skypro.homework.db.repository.AdRepository;
import ru.skypro.homework.db.repository.CommentRepository;
import ru.skypro.homework.db.repository.UserRepository;
import ru.skypro.homework.exception.AdsNotFoundException;
import ru.skypro.homework.exception.UserWithEmailNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.service.AdService;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ImageService imageService;

    @Autowired
    public AdServiceImpl(AdRepository adRepository, AdMapper adMapper, UserRepository userRepository,
                         CommentRepository commentRepository, ImageService imageService) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.imageService = imageService;
    }
    @Override
    public AdDto createAd(CreateOrUpdateAd createAds, String email, MultipartFile image) {
        Ad ad = adMapper.toAdsFromCreateAds(createAds);
        ad.setUser(userRepository.findByEmail(email)
                .orElseThrow(() -> new UserWithEmailNotFoundException(email)));
        ad.setImage(imageService.uploadImage(image, "/ads")); // вот эту строку надо будет заменить потом
        adRepository.save(ad);
        return adMapper.toDto(ad);
    }

    @Override
    public AdsDto getAllAds() {
        log.info("Поиск всех объявлений");
        List<Ad> adsList = adRepository.findAll();
        log.info("Передаем объявления в DTO");
        List<AdDto> adDtoList = adsList.stream()
                .map(adMapper::toDto)
                .toList();
        AdsDto adsDto = new AdsDto();
        adsDto.setCount(adsList.size());
        adsDto.setResults(adDtoList);
        log.info("Возвращаем результат");
        return adsDto;
    }
    @Override
    public ExtendedAd getAds(Long id) {
        Ad ads = adRepository.findById(id)
                .orElseThrow(() -> new AdsNotFoundException("Ads not found by id: " + id));
        return adMapper.toExtendedAds (ads);
    }

    @Override
    public AdsDto getAdsMe(String email) {
        log.info("Поиск всех объявлений для пользователя с email: " + email);
        List<Ad> adsList = adRepository.findByUser(userRepository.findByEmail(email)
                .orElseThrow(() -> new UserWithEmailNotFoundException(email)));
        log.info("Передаем объявления в DTO");
        List<AdDto> adDtoList = adsList.stream()
                .map(adMapper::toDto)
                .toList();
        AdsDto adsDto = new AdsDto();
        adsDto.setResults(adDtoList);
        adsDto.setCount(adsList.size());
        log.info("Возвращаем результат");
        return adsDto;
    }

    @Override
    public AdDto updateAd(CreateOrUpdateAd createOrUpdateAd, Long id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new AdsNotFoundException("Ads not found by id: " + id));
        adMapper.updateAd(createOrUpdateAd, ad);
        adRepository.save(ad);
        log.info("Обновлено объявление с id: " + id);
        return adMapper.toDto(ad);
    }

   @Transactional
    @Override
    public void removeAd(Long id) {
        commentRepository.deleteAllByAdId(id);
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new AdsNotFoundException("Ad not found by id: " + id));
        log.info("Removed Ads with id: " + id);
        adRepository.delete(ad);
    }

    @Override
    public void updateAdsImage(Long id, MultipartFile image) {
        Ad ads = adRepository.findById(id)
                .orElseThrow(() -> new AdsNotFoundException("Ads not found"));
        imageService.deleteFile(ads.getImage());
        ads.setImage(imageService.uploadImage(image, "/ads"));
        adRepository.save(ads);
    }


    @Override
    public byte[] getImage(String name) throws IOException {
        return imageService.downloadImage(name);
    }
}
