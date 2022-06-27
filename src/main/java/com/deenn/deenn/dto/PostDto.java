package com.deenn.deenn.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;

@Builder
@Getter @Setter
@AllArgsConstructor @RequiredArgsConstructor
public class PostDto {


    private Long id;

    @NotEmpty
    @Size(min = 2, message = "post should have at least two characters")
    private String title;
    @NotEmpty
    @Size(min = 10, message = "post should have at least ten characters")
    private String description;

    @NotEmpty
    private String content;
    private Collection<CommentDto> comments;

}
