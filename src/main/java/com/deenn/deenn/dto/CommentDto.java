package com.deenn.deenn.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class CommentDto {
    private long id;
    private String name;
    private String email;
    private String body;
}
