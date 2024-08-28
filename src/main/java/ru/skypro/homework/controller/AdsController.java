package ru.skypro.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.controller.dto.AdDto;
import ru.skypro.homework.controller.dto.AdsDto;
import ru.skypro.homework.controller.dto.CreateOrUpdateAd;
import ru.skypro.homework.service.AdService;

import java.util.Objects;

@RestController
@RequestMapping("/ads")
public class AdsController {

    private final AdService adService;

    @Autowired
    public AdsController(AdService adService) {
        this.adService = adService;
    }
    @GetMapping("/myAds")
    public ResponseEntity<AdsDto> getMyAds(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(adService.getMyAds(userDetails.getUsername()));
    }

    @PostMapping
    public ResponseEntity<AdDto> createAd(@AuthenticationPrincipal UserDetails userDetails,
                                          @RequestBody CreateOrUpdateAd createAds) {
        return ResponseEntity.ok(adService.createAd(createAds, userDetails.getUsername()));
    }

    @GetMapping
    public ResponseEntity<AdsDto> getAllAds() {
        AdsDto adsDto = adService.getAllAds();
        return new ResponseEntity<>(adsDto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdDto> updateAd(@AuthenticationPrincipal UserDetails userDetails,
                                          @RequestBody CreateOrUpdateAd createOrUpdateAdDto,
                                          @PathVariable Long id) {
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
                || adService.getMyAds(userDetails.getUsername()).getResults().stream().anyMatch(a -> Objects.equals(a.getPk(), id))) {
            return ResponseEntity.ok(adService.updateAd(createOrUpdateAdDto, id));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteAd(@AuthenticationPrincipal UserDetails userDetails,
                                               @PathVariable Long id) {
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
                || adService.getMyAds(userDetails.getUsername()).getResults().stream().anyMatch(a -> Objects.equals(a.getPk(), id))) {
            adService.removeAd(id);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
