package com.example.testypie.domain.comment.dto;

import com.example.testypie.domain.comment.entity.Comment;
import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public record CommentResponseDTO(
    Long id,
    String content,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt,
    @JsonIgnore
    User user,
    @JsonIgnore
    Product product
) {

    public CommentResponseDTO(Comment saveComment) {
        this(
            saveComment.getId(),
            saveComment.getContent(),
            saveComment.getCreateAt(),
            saveComment.getModifiedAt(),
            saveComment.getUser(),
            saveComment.getProduct()
        );
    }
}
