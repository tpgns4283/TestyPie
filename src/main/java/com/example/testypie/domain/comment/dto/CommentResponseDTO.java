package com.example.testypie.domain.comment.dto;

import com.example.testypie.domain.comment.entity.Comment;
import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

public record CommentResponseDTO(
        Long id,
        String content,
        Long commentLikeCnt,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        String account,
        @JsonIgnore
        Long productId
) {

    public static CommentResponseDTO of (Comment saveComment) {
        return new CommentResponseDTO(
                saveComment.getId(),
                saveComment.getContent(),
                saveComment.getCommentLikeCnt(),
                saveComment.getCreateAt(),
                saveComment.getModifiedAt(),
                saveComment.getUser().getAccount(),
                saveComment.getProduct().getId()
        );
    }
}
