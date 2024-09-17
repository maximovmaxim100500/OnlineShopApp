package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import ru.skypro.homework.controller.dto.CommentDTO;
import ru.skypro.homework.controller.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.db.entity.Comment;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        imports = {java.util.Date.class, ru.skypro.homework.db.entity.User.class}
)
public interface CommentMapper {

    @Mappings(value = {
            @Mapping(target = "author", expression = "java(comment.getUser().getId())"),
            @Mapping(target = "authorImage", expression = "java(comment.getUser().getImage())"),
            @Mapping(target = "authorFirstName", expression = "java(comment.getUser().getFirstName())"),
            @Mapping(target = "createdAt", expression = "java(comment.getCreatedAt().getTime())")
    })
    CommentDTO commentToCommentDTO(Comment comment);

    @Mappings(value = @Mapping(target = "createdAt", expression = "java(new Date())"))
    Comment createOrUpdateCommentDTOToComment(CreateOrUpdateCommentDTO commentDTO);

}
