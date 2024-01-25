package com.example.testypie.domain.commentLike.controller;

import com.example.testypie.domain.commentLike.dto.CommentLikeResponseDto;
import com.example.testypie.domain.commentLike.service.CommentLikeService;
import com.example.testypie.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api//category/{parentCategory_name}/"
        + "{childCategory_id}/products/{product_id}/comments")
public class commentLikeController {

    private final CommentLikeService commentLikeService;

    @PatchMapping("/{comment_id}")
    public ResponseEntity<CommentLikeResponseDto> clickCommentLike(
            @PathVariable String parentCategory_name,
            @PathVariable Long childCategory_id,
            @PathVariable Long product_id,
            @PathVariable Long comment_id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        CommentLikeResponseDto res = commentLikeService.clickCommentLike(comment_id,
                userDetails.getUser());

        return ResponseEntity.ok().body(res);
    }
}