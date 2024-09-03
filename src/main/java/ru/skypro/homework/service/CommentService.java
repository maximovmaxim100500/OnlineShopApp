package ru.skypro.homework.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.skypro.homework.controller.dto.CommentDTO;
import ru.skypro.homework.controller.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.db.entity.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> getAllCommentsByAdId(Integer id);

    Comment addCommentToAdByItsId(Integer id, String username, CreateOrUpdateCommentDTO commentDTO);

    HttpStatus deleteAdCommentByItsId(Integer adId, Integer commentId, String username);

    ResponseEntity<CommentDTO> updateAdCommentByItsId(
            Integer adId,
            Integer commentId,
            CreateOrUpdateCommentDTO commentDTO,
            String username
    );

}
