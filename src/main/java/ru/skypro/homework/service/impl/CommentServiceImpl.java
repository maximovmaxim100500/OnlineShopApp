package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.controller.dto.CommentDto;
import ru.skypro.homework.controller.dto.CommentsDto;
import ru.skypro.homework.controller.dto.CreateOrUpdateComment;
import ru.skypro.homework.db.entity.Ad;
import ru.skypro.homework.db.entity.Comment;
import ru.skypro.homework.db.repository.AdRepository;
import ru.skypro.homework.db.repository.CommentRepository;
import ru.skypro.homework.db.repository.UserRepository;
import ru.skypro.homework.exception.AdsNotFoundException;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.exception.UserWithEmailNotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AdRepository adRepository;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, AdRepository adRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.adRepository = adRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public CommentsDto getComments(Long id) {
        List<Comment> commentList = commentRepository.findAllByAdId(id);
        List<CommentDto> commentDtoList= commentList.stream()
                .map(commentMapper::toDto)
                .toList();
        CommentsDto commentsDto = new CommentsDto();
        commentsDto.setResults(commentDtoList);
        commentsDto.setCount(commentDtoList.size());
        return commentsDto;
    }

    @Override
    public CommentDto createComment(Long id, CreateOrUpdateComment createOrUpdateCommentDto, String email) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new AdsNotFoundException("Ads not found"));
        Comment comment = commentMapper.toCommentFromCreateComment(createOrUpdateCommentDto);
        comment.setAd(ad);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUser(userRepository.findByEmail(email).get());
        commentRepository.save(comment);
        log.info("Добавлен комментарий: " + comment.getId());
        return commentMapper.toDto(comment);
    }

    @Override
    @Transactional
    public void removeComment(Long adId, Long id) {
        commentRepository.deleteByAdIdAndId(adId, id);
        log.info("Удален комментарий с id: " + id);
    }

    @Override
    public CommentDto updateComment(Long adId, Long id, CreateOrUpdateComment createOrUpdateCommentDto) {
        log.info("Пытаемся найти комментарий к объявлению с adId: " + adId + " по id: " + id);
        Comment comment = commentRepository.findCommentByAdIdAndId(adId, id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));
        comment.setText(createOrUpdateCommentDto.getText());
        commentRepository.save(comment);
        log.info("Обновили комментарий с id: " + id);
        return commentMapper.toDto(comment);
    }

    @Override
    public CommentsDto getCommentsFromUserName(String userName) {
        log.info("Пытаемся найти все комментарии по пользователю с именем: " + userName);
        List<Comment> commentList = commentRepository.findAllByUser(userRepository.findByEmail(userName)
                .orElseThrow(() -> new UserWithEmailNotFoundException("User not found")));
        List<CommentDto> commentDtoList= commentList.stream()
                .map(commentMapper::toDto)
                .toList();
        CommentsDto commentsDto = new CommentsDto();
        commentsDto.setResults(commentDtoList);
        commentsDto.setCount(commentDtoList.size());
        return commentsDto;
    }

    @Override
    public String getUserNameOfComment(Long id) {
        log.info("Пытаемся найти комментарий для пользователя с id: " + id);
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"))
                .getUser().getEmail();
    }
}
