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

/**
 * Реализация сервиса для работы с изображениями объявлений.
 */
@Service
@RequiredArgsConstructor
public class AdImageServiceImpl implements AdImageService {
    private final AdImageRepository adImageRepository;
    @Value("${path.to.adimage.folder}") // Путь к папке для хранения изображений
    private String imagesFolder;

    /**
     * Создает и сохраняет изображение объявления.
     *
     * @param ad объявление, к которому привязано изображение
     * @param image изображение в формате MultipartFile
     * @return сохраненный объект AdImage
     * @throws IOException если произошла ошибка при работе с файловой системой
     */
    @Override
    public AdImage createAdImage(Ad ad, MultipartFile image) throws IOException {
        final Path filePath = Path.of(
                imagesFolder,
                ad.getPk() + "." + getExtension(image.getOriginalFilename())
        );

        Files.createDirectories(filePath.getParent()); // Создание директорий, если они не существуют
        Files.deleteIfExists(filePath); // Удаление старого файла, если он существует

        // Запись нового изображения в файл
        try (
                InputStream is = image.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }

        // Создание или обновление записи изображения в базе данных
        final AdImage adImage = adImageRepository.findById(ad.getPk())
                .orElseGet(AdImage::new);
        adImage.setPath(filePath.toString());
        adImage.setFileSize(image.getSize());
        adImage.setMediaType(image.getContentType());
        adImage.setAd(ad);

        return adImageRepository.save(adImage);
    }

    /**
     * Сохраняет объект AdImage в базе данных.
     *
     * @param image объект AdImage для сохранения
     * @return сохраненный объект AdImage
     */
    @Override
    public AdImage saveAdImage(AdImage image) {
        return adImageRepository.save(image);
    }

    /**
     * Получает изображение объявления и отправляет его в ответе.
     *
     * @param id идентификатор объявления
     * @param response объект HttpServletResponse для отправки изображения
     * @throws IOException если произошла ошибка при работе с файловой системой
     */
    @Override
    public void getAdImage(Integer id, HttpServletResponse response) throws IOException {
        Ad ad = Ad.builder()
                .pk(id)
                .build();
        AdImage adImage = adImageRepository.findByAd(ad)
                .orElseThrow(() -> new AdsImageFileNotFoundException("Image of ad with id " + id + " not found"));
        Path path = Path.of(adImage.getPath());

        // Отправка изображения в ответе
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream()) {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(adImage.getMediaType());
            response.setContentLength((int) adImage.getFileSize());
            is.transferTo(os);
        }
    }

    /**
     * Получает расширение файла из имени файла.
     *
     * @param fileName имя файла
     * @return расширение файла
     */
    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}
