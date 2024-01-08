package com.example.testypie.comment.dto;

import com.example.testypie.comment.entity.Comment;
import com.example.testypie.product.entity.Product;
import com.example.testypie.user.entity.User;
import java.time.LocalDateTime;

public record CommentResponseDTO(
    Long id,
    String content,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt,
    User user,
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
