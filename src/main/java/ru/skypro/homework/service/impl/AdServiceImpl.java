package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skypro.homework.controller.dto.AdDto;
import ru.skypro.homework.db.entity.Ad;
import ru.skypro.homework.db.repository.AdRepository;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.service.AdService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final AdMapper adMapper;

    @Autowired
    public AdServiceImpl(AdRepository adRepository, AdMapper adMapper) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
    }

    @Override
    public AdDto createAd(AdDto adDto) {
         return new AdDto();
//        Ad ad = adMapper.toEntity(adDto);
//        Ad savedAd = adRepository.save(ad);
//        return adMapper.toDto(savedAd);
    }

    @Override
    public AdDto getAdById(Long id) {
        return adRepository.findById(id)
                .map(adMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Ad not found"));
    }

    @Override
    public List<AdDto> getAllAds() {
        return adRepository.findAll().stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AdDto updateAd(Long id, AdDto adDto) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));
        adMapper.updateEntityFromDto(adDto, ad);
        Ad updatedAd = adRepository.save(ad);
        return adMapper.toDto(updatedAd);
    }

    @Override
    public void deleteAd(Long id) {
        adRepository.deleteById(id);
    }
}
