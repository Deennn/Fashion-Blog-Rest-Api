package com.deenn.deenn.service;

import com.deenn.deenn.dto.PostDto;
import com.deenn.deenn.dto.PostResponse;
import org.springframework.http.ResponseEntity;

public interface PostService {
    ResponseEntity<PostDto> createPost(PostDto postDto);
    ResponseEntity<PostResponse> getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
    ResponseEntity<PostDto> getPostById(long postId);
    ResponseEntity<PostDto> updatePost(long postId, PostDto postDto);
    ResponseEntity<String> deletePost(long postId);
}
