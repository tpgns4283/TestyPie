package com.example.testypie.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public record CommentRequestDTO(
        String content
) {
}
