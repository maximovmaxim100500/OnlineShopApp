package ru.skypro.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.controller.dto.AdDto;
import ru.skypro.homework.controller.dto.AdsDto;
import ru.skypro.homework.controller.dto.CreateOrUpdateAd;
import ru.skypro.homework.controller.dto.ExtendedAd;
import ru.skypro.homework.service.AdService;

import java.io.IOException;
import java.util.Objects;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
public class AdsController {

    private final AdService adService;

    @Autowired
    public AdsController(AdService adService) {
        this.adService = adService;
    }

    @GetMapping("/me")
    public ResponseEntity<AdsDto> getMyAds(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(adService.getMyAds(userDetails.getUsername()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAds(@PathVariable Long id) {
        return ResponseEntity.ok(adService.getAds(id));
    }
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdDto> createAd(@AuthenticationPrincipal UserDetails userDetails,
                                          @RequestPart("properties") CreateOrUpdateAd createAds,
                                          @RequestPart("image") MultipartFile image) {
        return ResponseEntity.ok(adService.createAd(createAds, userDetails.getUsername(), image));
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

    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateAdsImage(@AuthenticationPrincipal UserDetails userDetails,
                                            @PathVariable Long id,
                                            @RequestParam MultipartFile image) {
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
                || adService.getMyAds(userDetails.getUsername()).getResults().stream().anyMatch(a -> Objects.equals(a.getPk(), id))) {
            adService.updateAdsImage(id, image);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    @GetMapping(value = "/image/{name}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImages(@PathVariable String name) throws IOException {
        return adService.getImage(name);
    }
}
