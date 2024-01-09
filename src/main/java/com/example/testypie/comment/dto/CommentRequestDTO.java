package com.example.testypie.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentRequestDTO {
    @NotBlank
    private String content;
}
