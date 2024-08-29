package ru.skypro.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.controller.dto.CommentDto;
import ru.skypro.homework.controller.dto.CommentsDto;
import ru.skypro.homework.controller.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;

import java.util.Objects;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<CommentsDto> getComments(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getComments(id));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDto> addComment(@PathVariable Long id,
                                                 @RequestBody CreateOrUpdateComment createOrUpdateCommentDto,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(commentService.createComment(id, createOrUpdateCommentDto, userDetails.getUsername()));
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal UserDetails userDetails,
                                           @PathVariable Long adId, @PathVariable Long commentId) {

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
                || commentService.getCommentsFromUserName(userDetails.getUsername()).getResults().stream()
                .anyMatch(a -> Objects.equals(a.getPk(), commentId))) {
            commentService.removeComment(adId, commentId);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@AuthenticationPrincipal UserDetails userDetails,
                                                    @PathVariable Long adId,
                                                    @PathVariable Long commentId,
                                                    @RequestBody CreateOrUpdateComment createOrUpdateCommentDto) {
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
                || commentService.getCommentsFromUserName(userDetails.getUsername()).getResults().stream()
                .anyMatch(a -> Objects.equals(a.getPk(), commentId))) {
            return ResponseEntity.ok(commentService.updateComment(adId, commentId, createOrUpdateCommentDto));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
