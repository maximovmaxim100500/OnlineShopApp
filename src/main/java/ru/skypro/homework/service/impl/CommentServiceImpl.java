package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.controller.dto.CommentDTO;
import ru.skypro.homework.controller.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.controller.dto.enums.Role;
import ru.skypro.homework.exception.AdsCommentNotFoundException;
import ru.skypro.homework.db.entity.Ad;
import ru.skypro.homework.db.entity.Comment;
import ru.skypro.homework.db.entity.User;
import ru.skypro.homework.db.repository.CommentRepository;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

import java.util.List;

/**
 * Реализация сервиса для работы с комментариями.
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdsService adsService;
    private final UserService userService;
    private final CommentMapper commentMapper;

    /**
     * Получает все комментарии для объявления по его идентификатору.
     *
     * @param id идентификатор объявления
     * @return список комментариев для указанного объявления
     */
    @Override
    public List<Comment> getAllCommentsByAdId(Integer id) {
        Ad ad = adsService.getAdById(id);

        return commentRepository.findAllByAd(ad);
    }

    /**
     * Добавляет комментарий к объявлению по его идентификатору.
     *
     * @param id идентификатор объявления
     * @param username имя пользователя, добавляющего комментарий
     * @param commentDTO данные комментария
     * @return добавленный комментарий
     */
    @Override
    public Comment addCommentToAdByItsId(Integer id, String username, CreateOrUpdateCommentDTO commentDTO) {
        Ad ad = adsService.getAdById(id);
        User user = userService.findUserByEmail(username);

        Comment newComment = commentMapper.createOrUpdateCommentDTOToComment(commentDTO);
        newComment.setUser(user);
        newComment.setAd(ad);

        return commentRepository.save(newComment);
    }

    /**
     * Удаляет комментарий по его идентификатору.
     *
     * @param adId идентификатор объявления
     * @param commentId идентификатор комментария
     * @param username имя пользователя, запрашивающего удаление
     * @return статус удаления
     */
    @Override
    @Transactional
    public HttpStatus deleteAdCommentByItsId(Integer adId, Integer commentId, String username) {
        Ad foundAd = adsService.getAdById(adId);
        Comment foundComment = findCommentById(commentId);

        if (!foundComment.getAd().equals(foundAd)) {
            return HttpStatus.NOT_FOUND;
        }

        User foundUser = userService.findUserByEmail(username);
        Role userRole = foundUser.getRole();
        boolean isCommentAuthor = (foundComment.getUser() == foundUser);
        boolean userHasPermit = (isCommentAuthor || userRole == Role.ADMIN);

        if (userHasPermit) {
            commentRepository.delete(commentId);
            return HttpStatus.OK;
        } else {
            return HttpStatus.FORBIDDEN;
        }
    }


    /**
     * Обновляет комментарий по его идентификатору.
     *
     * @param adId идентификатор объявления
     * @param commentId идентификатор комментария
     * @param commentDTO данные для обновления комментария
     * @param username имя пользователя, запрашивающего обновление
     * @return обновленный комментарий в формате {@link ResponseEntity}
     */
    @Override
    public ResponseEntity<CommentDTO> updateAdCommentByItsId(
            Integer adId,
            Integer commentId,
            CreateOrUpdateCommentDTO commentDTO,
            String username
    ) {
        Ad foundAd = adsService.getAdById(adId);
        Comment foundComment = findCommentById(commentId);

        if (!foundComment.getAd().equals(foundAd)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User foundUser = userService.findUserByEmail(username);
        Role userRole = foundUser.getRole();
        boolean isCommentAuthor = (foundComment.getUser() == foundUser);
        boolean userHasPermit = (isCommentAuthor || userRole == Role.ADMIN);

        if (userHasPermit) {
            Comment updateComment = commentMapper.createOrUpdateCommentDTOToComment(commentDTO);

            foundComment.setText(updateComment.getText());
            foundComment.setUser(foundUser);
            foundComment.setCreatedAt(updateComment.getCreatedAt());

            Comment savedComment = commentRepository.save(foundComment);

            return ResponseEntity.ok(commentMapper.commentToCommentDTO(savedComment));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * Находит комментарий по его идентификатору.
     *
     * @param id идентификатор комментария
     * @return найденный комментарий
     * @throws AdsCommentNotFoundException если комментарий не найден
     */
    private Comment findCommentById(Integer id) {
        return commentRepository.findByPk(id)
                .orElseThrow(() -> new AdsCommentNotFoundException("Comment with id " + id + " not found."));
    }

}
