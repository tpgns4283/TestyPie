package com.example.testypie.comment.controller;

import com.example.testypie.comment.dto.CommentRequestDTO;
import com.example.testypie.comment.dto.CommentResponseDTO;
import com.example.testypie.comment.service.CommentService;
import com.example.testypie.security.UserDetailsImpl;
import com.example.testypie.user.entity.User;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/products/{product_id}/comment")
@RestController
public class CommnetController {

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
