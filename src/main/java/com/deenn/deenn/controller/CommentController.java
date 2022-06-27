package com.deenn.deenn.controller;

import com.deenn.deenn.dto.CommentDto;
import com.deenn.deenn.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("posts/{postId}/comments")
    public ResponseEntity<CommentDto> makeComment(@Valid @RequestBody CommentDto commentDto, @PathVariable long postId) {
        return commentService.createComment(postId, commentDto);
    }

    @GetMapping("posts/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentByPostId(@PathVariable long postId) {
        return commentService.getCommentByPostId(postId);
    }

    @GetMapping("posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable long postId, @PathVariable long commentId) {
        return commentService.getCommentById(postId, commentId);
    }

    @PutMapping("posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@Valid @PathVariable long postId, @PathVariable long commentId, @RequestBody CommentDto commentDto) {
        return commentService.updateComment(postId, commentId, commentDto);
    }
    @DeleteMapping("posts/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable long postId, @PathVariable long commentId) {
        return commentService.deleteComment(postId, commentId);
    }

}
