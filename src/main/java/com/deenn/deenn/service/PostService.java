package com.deenn.deenn.service;

import com.deenn.deenn.dto.PostDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PostService {
    ResponseEntity<PostDto> createPost(PostDto postDto);
    ResponseEntity<List<PostDto>> getAllPosts(int pageNo, int pageSize);
    ResponseEntity<PostDto> getPostById(long postId);
    ResponseEntity<PostDto> updatePost(long postId, PostDto postDto);
    ResponseEntity<String> deletePost(long postId);
}
