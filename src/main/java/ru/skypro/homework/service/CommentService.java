package ru.skypro.homework.service;

import ru.skypro.homework.controller.dto.CommentDto;
import ru.skypro.homework.controller.dto.CommentsDto;
import ru.skypro.homework.controller.dto.CreateOrUpdateComment;


public interface CommentService {
    CommentsDto getComments(Long id);

    CommentDto createComment(Long id, CreateOrUpdateComment createComment, String email);

    void removeComment(Long adId, Long id);

    CommentDto updateComment(Long adId, Long id, CreateOrUpdateComment createComment);
    CommentsDto getCommentsFromUserName(String userName);
    String getUserNameOfComment(Long id);
}
