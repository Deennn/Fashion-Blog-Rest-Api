package com.deenn.deenn.service.impl;

import com.deenn.deenn.dto.CommentDto;
import com.deenn.deenn.entity.Comment;
import com.deenn.deenn.entity.Post;
import com.deenn.deenn.exception.BlogApiException;
import com.deenn.deenn.exception.ResourceNotFoundException;
import com.deenn.deenn.repository.CommentRepository;
import com.deenn.deenn.repository.PostRepository;
import com.deenn.deenn.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {


    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    private final ModelMapper mapper;

    public CommentServiceImpl(PostRepository postRepository, CommentRepository commentRepository, ModelMapper mapper) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<CommentDto> createComment(long postId, CommentDto commentDto) {

        Comment comment = mapToEntity(commentDto);
        Post postToBeCommented = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("post", "id", postId));
        comment.setPost(postToBeCommented);

        Comment savedComment = commentRepository.save(comment);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDto(savedComment));
    }

    @Override
    public ResponseEntity<List<CommentDto>> getCommentByPostId(long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        List<CommentDto> commentDtoList = comments.stream().map(this::mapToDto).collect(Collectors.toList());

        return ResponseEntity.ok(commentDtoList);

    }

    @Override
    public ResponseEntity<CommentDto> getCommentById(long postId, long commentId) {
        Comment comment = CheckerMethod(postId, commentId);
        return ResponseEntity.ok(mapToDto(comment));

    }

    @Override
    public ResponseEntity<CommentDto> updateComment(long postId, long commentId, CommentDto commentRequest) {
        Comment comment = CheckerMethod(postId, commentId);

        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());

        Comment updatedComment = commentRepository.save(comment);

        return ResponseEntity.ok(mapToDto(updatedComment));
    }

    private Comment CheckerMethod(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("post","id", postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("comment","id", commentId));

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        return comment;
    }

    @Override
    public ResponseEntity<String> deleteComment(long postId, long commentId) {
        Comment comment = CheckerMethod(postId, commentId);
        commentRepository.delete(comment);
        return ResponseEntity.ok("Comment deleted successfully");
    }

    private CommentDto mapToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getCommentId())
                .name(comment.getName())
                .email(comment.getEmail())
                .body(comment.getBody())
                .build();
    }

    private Comment mapToEntity(CommentDto commentDto) {
        return Comment.builder()
                .name(commentDto.getName())
                .email(commentDto.getEmail())
                .body(commentDto.getBody())
                .build();
    }
}
