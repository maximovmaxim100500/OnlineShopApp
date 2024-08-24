package ru.skypro.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.controller.dto.AdDto;
import ru.skypro.homework.service.AdService;

import java.util.List;

@RestController
@RequestMapping("/ads")
public class AdsController {

    private final AdService adService;

    @Autowired
    public AdsController(AdService adService) {
        this.adService = adService;
    }

    @PostMapping
    public ResponseEntity<AdDto> createAd(@RequestBody AdDto adDto) {
        AdDto createdAd = adService.createAd(adDto);
        return new ResponseEntity<>(createdAd, HttpStatus.CREATED);
//        return ResponseEntity.ok(new AdDto());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdDto> getAdById(@PathVariable Long id) {
        AdDto adDto = adService.getAdById(id);
        return new ResponseEntity<>(adDto, HttpStatus.OK);
//        return ResponseEntity.ok(new AdDto());
    }

    @GetMapping
    public ResponseEntity<List<AdDto>> getAllAds() {
        List<AdDto> adDtos = adService.getAllAds();
        return new ResponseEntity<>(adDtos, HttpStatus.OK);
//        return ResponseEntity.ok(new ArrayList<AdDto>());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdDto> updateAd(@PathVariable Long id, @RequestBody AdDto adDto) {
        AdDto updatedAd = adService.updateAd(id, adDto);
        return new ResponseEntity<>(updatedAd, HttpStatus.OK);
//        return ResponseEntity.ok(new AdDto());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable Long id) {
        adService.deleteAd(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
