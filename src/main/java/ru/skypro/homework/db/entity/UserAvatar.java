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
    private Integer id;
    @Column(name = "file_size")
    private long fileSize;
    @Column(name = "media_type")
    private String mediaType;
    @Lob
    @ToString.Exclude
    private byte[] data;
    @OneToOne
    private User user;

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
