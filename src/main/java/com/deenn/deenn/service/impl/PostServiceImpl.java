package com.deenn.deenn.service.impl;

import com.deenn.deenn.dto.PostDto;
import com.deenn.deenn.entity.Post;
import com.deenn.deenn.exception.ResourceNotFoundException;
import com.deenn.deenn.repository.PostRepository;
import com.deenn.deenn.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    @Override
    public ResponseEntity<PostDto> createPost(PostDto postDto) {
        Post post = postRepository.save(Post.builder()
                        .title(postDto.getTitle())
                        .description(postDto.getDescription())
                        .content(postDto.getContent())
                        .build());

        return ResponseEntity.status(HttpStatus.CREATED).body(PostDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .description(post.getDescription())
                        .content(post.getContent())
                        .build());
    }

    @Override
    public ResponseEntity<List<PostDto>> getAllPosts() {
        return ResponseEntity.ok( postRepository.findAll().stream().map(post -> PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .content(post.getContent())
                .build()).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<PostDto> getPostById(long postId) {
       return ResponseEntity.ok( postRepository.findById(postId).map(post -> PostDto.builder()
               .id(post.getId())
               .title(post.getTitle())
               .description(post.getDescription())
               .content(post.getContent())
               .build()).orElseThrow( () -> new ResourceNotFoundException("post", "postId", postId)));
    }

    @Override
    public ResponseEntity<PostDto> updatePost(long postId, PostDto postDto) {
      Post postTobeUpdated = postRepository.findById(postId)
              .orElseThrow(() -> new ResourceNotFoundException("post", "postId", postId));
      postTobeUpdated.setTitle(postDto.getTitle());
      postTobeUpdated.setDescription(postDto.getDescription());
      postTobeUpdated.setContent(postDto.getContent());
      Post newPost = postRepository.save(postTobeUpdated);
      return ResponseEntity.ok(PostDto.builder()
                      .id(newPost.getId())
                      .title(newPost.getTitle())
                      .description(newPost.getDescription())
                      .content(newPost.getContent())
                      .build());

    }

    @Override
    public ResponseEntity<String> deletePost(long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("post", "postId", postId));
        postRepository.delete(post);
        return ResponseEntity.ok("Post deleted successfully");
    }
}
