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
@RequestMapping("/api")
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/category/{parentCategory_name}/{childCategory_id}/products/{product_id}/comments")
    public ResponseEntity<CommentResponseDTO> postComment(
            @PathVariable Long product_id,
            @Valid @RequestBody CommentRequestDTO req,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long childCategory_id,
            @PathVariable String parentCategory_name) {
        CommentResponseDTO res = commentService.productComment(product_id, req, userDetails.getUser(), childCategory_id, parentCategory_name);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/category/{parentCategory_name}/{childCategory_id}/products/{product_id}/comments")
    public ResponseEntity<List<CommentResponseDTO>> getComment(@PathVariable Long product_id,
                                                               @PathVariable Long childCategory_id,
                                                               @PathVariable String parentCategory_name) {
        List<CommentResponseDTO> resList = commentService.getComments(product_id, childCategory_id, parentCategory_name);
        return ResponseEntity.ok(resList);
    }

    @PatchMapping("/category/{parentCategory_name}/{childCategory_id}/products/{product_id}/comments/{comment_id}")
    public ResponseEntity<CommentResponseDTO> updateComment(
            @PathVariable Long product_id,
            @PathVariable Long comment_id,
            @Valid @RequestBody CommentRequestDTO req,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long childCategory_id,
            @PathVariable String parentCategory_name) {
        CommentResponseDTO res = commentService.updateComment(product_id, comment_id, req, userDetails.getUser(), childCategory_id, parentCategory_name);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/category/{parentCategory_name}/{childCategory_id}/products/{product_id}/comments/{comment_id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long product_id,
            @PathVariable Long comment_id,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long childCategory_id,
            @PathVariable String parentCategory_name){
     commentService.deleteComment(product_id, comment_id, userDetails.getUser(), childCategory_id, parentCategory_name);
     return ResponseEntity.noContent().build();
    }
}
