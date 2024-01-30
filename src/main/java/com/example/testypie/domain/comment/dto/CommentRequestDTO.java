package com.example.testypie.domain.comment.dto;

import lombok.NonNull;

public record CommentRequestDTO(
        @NonNull
        String content
) {

}
