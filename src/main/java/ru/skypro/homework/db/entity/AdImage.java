package ru.skypro.homework.db.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "images")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class AdImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                     // Уникальный идентификатор изображения
    private String path;                    // Путь к изображению
    @Column(name = "file_size")
    private long fileSize;                  // Размер файла изображения в байтах
    @Column(name = "media_type")
    private String mediaType;               // Медиа тип изображения (например, "image/jpeg")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ad_id")
    private Ad ad;                          // Объявление, к которому относится это изображение

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdImage adImage = (AdImage) o;
        return fileSize == adImage.fileSize
                && Objects.equals(path, adImage.path)
                && Objects.equals(mediaType, adImage.mediaType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, fileSize, mediaType);
    }
}