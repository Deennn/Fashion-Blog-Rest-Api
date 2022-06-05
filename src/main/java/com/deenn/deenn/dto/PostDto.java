package com.deenn.deenn.dto;

import lombok.*;

@Builder
@Getter @Setter
@AllArgsConstructor @RequiredArgsConstructor
public class PostDto {


    private Long id;
    private String title;
    private String description;
    private String content;

}
