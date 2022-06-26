package com.deenn.deenn.service;

import com.deenn.deenn.dto.CommentDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommentService {

    ResponseEntity<CommentDto> createComment(long postId, CommentDto commentDto);
    ResponseEntity<List<CommentDto>> getCommentByPostId(long postId);
    ResponseEntity<CommentDto> getCommentById(long postId, long commentId);
    ResponseEntity<CommentDto> updateComment(long postId, long commentId, CommentDto commentRequest);
    ResponseEntity<String> deleteComment(long postId, long commentId);
}
