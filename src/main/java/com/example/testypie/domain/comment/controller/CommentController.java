package com.example.testypie.domain.comment.controller;

import com.example.testypie.domain.comment.dto.CommentRequestDTO;
import com.example.testypie.domain.comment.dto.CommentResponseDTO;
import com.example.testypie.domain.comment.service.CommentService;
import com.example.testypie.global.security.UserDetailsImpl;
import jakarta.validation.Valid;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/products/{product_id}/comment")
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDTO> postComment(
        @PathVariable Long product_id,
        @Valid @RequestBody CommentRequestDTO req,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        CommentResponseDTO res = commentService.productComment(product_id, req, userDetails.getUser());
        return ResponseEntity.ok(res);
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDTO>> getComment(@PathVariable Long product_id) {
        List<CommentResponseDTO> resList = commentService.getComments(product_id);
        return ResponseEntity.ok(resList);
    }

    @PatchMapping("/{comment_id}")
    public ResponseEntity<CommentResponseDTO> updateComment(
        @PathVariable Long product_id,
        @PathVariable Long comment_id,
        @Valid @RequestBody CommentRequestDTO req,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        CommentResponseDTO res = commentService.updateComment(product_id, comment_id, req, userDetails.getUser());
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{comment_id}")
    public ResponseEntity<Void> deleteComment(
        @PathVariable Long product_id,
        @PathVariable Long comment_id,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
     commentService.deleteComment(product_id, comment_id, userDetails.getUser());
     return ResponseEntity.noContent().build();
    }
}