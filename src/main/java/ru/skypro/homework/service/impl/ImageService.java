package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {
    /**
     * Директория для хранения изображений, значение считывается из конфигурации приложения.
     */
    @Value("${image.dir.path}")
    private String imageDir;

    /**
     * Загружает изображение на сервер и сохраняет его в указанной директории.
     *
     * @param image объект {@link MultipartFile} представляющий загружаемое изображение.
     * @param name имя пользователя или уникальный идентификатор, используемый в пути сохранения изображения.
     * @return строка содержащая путь к загруженному изображению.
     */
    public String uploadImage(MultipartFile image, String name) {
        // Получаем расширение файла
        String extension = StringUtils.getFilenameExtension(image.getOriginalFilename());
        // Создаем уникальное имя файла с использованием UUID и расширения файла
        String filename = UUID.randomUUID() + "." + extension;
        // Определяем путь к файлу в директории для изображений
        Path filePath = Path.of(imageDir, filename);
        try {
            // Записываем байты изображения в файл
            Files.write(filePath, image.getBytes());
        } catch (IOException e) {
            // Логируем и пробрасываем исключение в случае ошибки записи файла
            log.error("Error writing file: {}", e.getMessage());
            throw new RuntimeException("Error writing file", e);
        }
        log.info("Загружен файл, имя файла " + filename);
        // Возвращаем путь к изображению относительно директории пользователя
        return name + "/image/" + filename;
    }

    /**
     * Загружает изображение с сервера по имени файла.
     *
     * @param name имя файла изображения, которое нужно загрузить.
     * @return массив байтов изображения.
     * @throws IOException если возникает ошибка при чтении файла.
     */
    public byte[] downloadImage(String name) throws IOException {
        // Определяем полный путь к файлу изображения
        String fullPath = imageDir + "/" + name;
        File file = new File(fullPath);
        log.info("Загрузка файла " + fullPath);
        if (file.exists()) {
            // Читаем и возвращаем байты изображения, если файл существует
            return Files.readAllBytes(Path.of(fullPath));
        }
        log.info("Файл не найден " + fullPath);
        // Возвращаем null, если файл не найден
        return null;
    }

    /**
     * Удаляет файл изображения с сервера.
     *
     * @param path путь к изображению, которое нужно удалить.
     */
    public void deleteFile(String path) {
        // Проверяем, что путь не равен null
        if (path == null) {
            return;
        }
        // Извлекаем имя файла из пути
        String fileName = path.substring(path.lastIndexOf('/'));
        File fileToDelete = new File(imageDir + fileName);
        if (fileToDelete.exists()) {
            // Удаляем файл, если он существует, и логируем результат
            if (fileToDelete.delete()) {
                log.info("Файл успешно удален");
            } else {
                log.info("Не удалось удалить файл");
            }
        } else {
            log.info("Файл по пути " + path + " не найден");
        }
    }
}
