package com.deenn.deenn.service.impl;

import com.deenn.deenn.dto.PostDto;
import com.deenn.deenn.dto.PostResponse;
import com.deenn.deenn.entity.Post;
import com.deenn.deenn.exception.ResourceNotFoundException;
import com.deenn.deenn.repository.PostRepository;
import com.deenn.deenn.service.PostService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<PostResponse> getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable  = PageRequest.of(pageNo, pageSize, sort);
        var posts = postRepository.findAll(pageable);

        List<PostDto> postDtoList = posts.getContent().stream().map(post -> PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .content(post.getContent())
                .build()).collect(Collectors.toList());


        return ResponseEntity.ok(PostResponse.builder()
                .content(postDtoList)
                .pageNo(posts.getNumber())
                .pageSize(posts.getSize())
                .totalElements(posts.getNumberOfElements())
                .totalPages(posts.getTotalPages())
                .last(posts.isLast())
                .build());
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
