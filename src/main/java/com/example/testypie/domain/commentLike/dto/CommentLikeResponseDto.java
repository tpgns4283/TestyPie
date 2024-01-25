package com.example.testypie.domain.commentLike.dto;

public record CommentLikeResponseDto(

        Boolean isCommentLiked
) {

    public static CommentLikeResponseDto of(Boolean isCommentLiked) {

        return new CommentLikeResponseDto(isCommentLiked);
    }
}