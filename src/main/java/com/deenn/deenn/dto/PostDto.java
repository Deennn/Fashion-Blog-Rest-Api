package com.deenn.deenn.dto;

import lombok.*;

import java.util.Collection;

@Builder
@Getter @Setter
@AllArgsConstructor @RequiredArgsConstructor
public class PostDto {


    private Long id;
    private String title;
    private String description;
    private String content;
    private Collection<CommentDto> comments;

}
