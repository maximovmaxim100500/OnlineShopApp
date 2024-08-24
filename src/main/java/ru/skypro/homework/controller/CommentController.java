package ru.skypro.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.controller.dto.CommentDto;
import ru.skypro.homework.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto) {
        CommentDto createdComment = commentService.createComment(commentDto);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
//        return ResponseEntity.ok(new CommentDto());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long id) {
        CommentDto commentDto = commentService.getCommentById(id);
        return new ResponseEntity<>(commentDto, HttpStatus.OK);
        //       return ResponseEntity.ok(new CommentDto());
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllComments() {
        List<CommentDto> commentDtos = commentService.getAllComments();
        return new ResponseEntity<>(commentDtos, HttpStatus.OK);
//        return ResponseEntity.ok(new ArrayList<CommentDto>());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long id, @RequestBody CommentDto commentDto) {
        CommentDto updatedComment = commentService.updateComment(id, commentDto);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
//        return ResponseEntity.ok(new CommentDto());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
