package com.example.testypie.comment.dto;

import jakarta.validation.constraints.NotBlank;

public class CommentRequestDTO {
    @NotBlank
    private String content;
}
