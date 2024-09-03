package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.AdsImageFileNotFoundException;
import ru.skypro.homework.db.entity.Ad;
import ru.skypro.homework.db.entity.AdImage;
import ru.skypro.homework.db.repository.AdImageRepository;
import ru.skypro.homework.service.AdImageService;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@RequiredArgsConstructor
public class AdImageServiceImpl implements AdImageService {
    private final AdImageRepository adImageRepository;
    @Value("${path.to.adimage.folder}")
    private String imagesFolder;

    @Override
    public AdImage createAdImage(Ad ad, MultipartFile image) throws IOException {
        final Path filePath = Path.of(
                imagesFolder,
                ad.getPk() + "." + getExtension(image.getOriginalFilename())
        );

        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (
                InputStream is = image.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }

        final AdImage adImage = adImageRepository.findById(ad.getPk())
                .orElseGet(AdImage::new);
        adImage.setPath(filePath.toString());
        adImage.setFileSize(image.getSize());
        adImage.setMediaType(image.getContentType());
        adImage.setAd(ad);

        return adImageRepository.save(adImage);
    }

    @Override
    public AdImage saveAdImage(AdImage image) {
        return adImageRepository.save(image);
    }

    @Override
    public void getAdImage(Integer id, HttpServletResponse response) throws IOException {
        Ad ad = Ad.builder()
                .pk(id)
                .build();
        AdImage adImage = adImageRepository.findByAd(ad)
                .orElseThrow(() -> new AdsImageFileNotFoundException("Image of ad with id " + id + " not found"));
        Path path = Path.of(adImage.getPath());

        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream()) {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(adImage.getMediaType());
            response.setContentLength((int) adImage.getFileSize());
            is.transferTo(os);
        }
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}
