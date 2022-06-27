package com.deenn.deenn.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter @Setter
@Builder
public class CommentDto {
    private long id;

    @NotEmpty(message = "name should not be empty")
    private String name;

    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    @Size(min = 10, message = "post should have at least ten characters")
    private String body;
}
