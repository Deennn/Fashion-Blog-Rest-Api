package com.deenn.deenn.service.impl;

import com.deenn.deenn.dto.PostDto;
import com.deenn.deenn.dto.PostResponse;
import com.deenn.deenn.entity.Post;
import com.deenn.deenn.exception.ResourceNotFoundException;
import com.deenn.deenn.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postService;

    PostDto postDto;
    Post post;
    Sort sort;
    Pageable pageable;

    int pageNo;
    int pageSize;
    String sortBy;
    String sortDir;
    List<Post> posts = List.of(new Post());
    Page<Post> pageOfPost = new PageImpl<>(posts);

    @BeforeEach
    void setUp() {
        postDto = PostDto.builder()
                .title("first")
                .description("first description")
                .content("first content")
                .build();

        post = Post.builder()
                .title(postDto.getTitle())
                .description(postDto.getDescription())
                .content(postDto.getContent())
                .build();

    }

    @Test
    void createPost() {

        given(postRepository.save(post)).willReturn(post);

        var savedPost = postService.createPost(postDto);

        Assertions.assertThat(savedPost).isNotNull();
        assertEquals(Objects.requireNonNull(savedPost.getBody()).getTitle(), post.getTitle());
        assertEquals(savedPost.getStatusCode(), HttpStatus.CREATED);
        verify(postRepository, atLeastOnce()).save(post);
    }

    @Test
    void getAllPosts() {
        sortBy = "Title";
        sortDir = "asc";
        pageNo  = 1;
        pageSize = 10;
        sort = Sort.by(sortBy).ascending();
        pageable  = PageRequest.of(pageNo, pageSize, sort);
        given(postRepository.findAll(pageable)).willReturn(pageOfPost);

        ResponseEntity<PostResponse> postResponseResponseEntity = postService.getAllPosts(pageNo, pageSize, sortBy, sortDir );

        Assertions.assertThat(postResponseResponseEntity.getBody()).isNotNull();
        assertEquals(postResponseResponseEntity.getStatusCode(), HttpStatus.OK);
        verify(postRepository, atLeastOnce()).findAll(pageable);

    }

    @Test
    void getPostById() {
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        ResponseEntity<PostDto> postDtoResponseEntity = postService.getPostById(1L);

        Assertions.assertThat(postDtoResponseEntity).isNotNull();
        assertEquals(postDtoResponseEntity.getStatusCode(), HttpStatus.OK);
        assertEquals(Objects.requireNonNull(postDtoResponseEntity.getBody()).getTitle(), postDto.getTitle());
        verify(postRepository, atLeastOnce()).findById(anyLong());
    }

    @DisplayName("Get Post By Id when you cant find the post")
    @Test
    void getPostById_And_Throw_Exception() {
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> postService.getPostById(1L));

    }

    @Test
    void updatePost() {
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(postRepository.save(any())).willReturn(post);
        ResponseEntity<PostDto> postDtoResponseEntity = postService.updatePost(1l, postDto);
        Assertions.assertThat(postDtoResponseEntity.getBody()).isNotNull();
        org.junit.jupiter.api.Assertions.assertEquals(postDtoResponseEntity.getStatusCode(), HttpStatus.OK);
        verify(postRepository, times(1)).save(post);
        verify(postRepository, times(1)).findById(anyLong());

    }
    @DisplayName("Should throw exceptions when product not found")
    @Test
    void updatePost_Should_Throw () {
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> postService.updatePost(1l, postDto));
        verify(postRepository, atLeastOnce()).findById(anyLong());
        verify(postRepository, never()).save(post);

    }

    @Test
    void deletePost() {
        willDoNothing().given(postRepository).deleteById(anyLong());
        ResponseEntity<String> responseEntity = postService.deletePost(1L);
        org.junit.jupiter.api.Assertions.assertEquals(responseEntity.getBody(), "Post deleted successfully");
        org.junit.jupiter.api.Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        verify(postRepository, times(1)).deleteById(anyLong());

    }
}