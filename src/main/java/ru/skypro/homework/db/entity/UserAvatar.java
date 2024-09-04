package ru.skypro.homework.db.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "avatars")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserAvatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;             // Уникальный идентификатор аватара
    @Column(name = "file_size")
    private long fileSize;          // Размер файла аватара в байтах
    @Column(name = "media_type")
    private String mediaType;       // Тип медиа-файла (например, image/png)
    @Lob
    @ToString.Exclude
    private byte[] data;            // Данные аватара в виде массива байтов
    @OneToOne
    private User user;              // Связь с пользователем, которому принадлежит этот аватар

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAvatar that = (UserAvatar) o;
        return fileSize == that.fileSize
                && Objects.equals(mediaType, that.mediaType)
                && Objects.deepEquals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileSize, mediaType, Arrays.hashCode(data));
    }

    @Override
    public String toString() {
        return "UserAvatar{" +
                "id=" + id +
                ", fileSize=" + fileSize +
                ", mediaType='" + mediaType + '\'' +
                ", hashCode(data)=" + Arrays.hashCode(data) +
                ", user=" + user +
                '}';
    }

}
