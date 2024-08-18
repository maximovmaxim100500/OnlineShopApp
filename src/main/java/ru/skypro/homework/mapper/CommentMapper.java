package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.controller.dto.CommentDto;
import ru.skypro.homework.db.entity.Comment;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "user.id", target = "author")
    @Mapping(source = "user.image", target = "authorImage")
    @Mapping(source = "user.firstName", target = "authorFirstName")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "localDateTimeToInteger")
    CommentDto toDto(Comment comment);

    @Mapping(source = "pk", target = "id")
    @Mapping(source = "author", target = "user.id")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "integerToLocalDateTime")
    Comment toEntity(CommentDto commentDto);

    @Mapping(source = "pk", target = "id")
    @Mapping(source = "author", target = "user.id")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "integerToLocalDateTime")
    void updateEntityFromDto(CommentDto commentDto, @MappingTarget Comment comment);

    @Named("localDateTimeToInteger")
    static Integer localDateTimeToInteger(LocalDateTime localDateTime) {
        return localDateTime == null ? null : (int) localDateTime.toEpochSecond(ZoneOffset.UTC);
    }

    @Named("integerToLocalDateTime")
    static LocalDateTime integerToLocalDateTime(Integer timestamp) {
        return timestamp == null ? null : LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC);
    }
}
