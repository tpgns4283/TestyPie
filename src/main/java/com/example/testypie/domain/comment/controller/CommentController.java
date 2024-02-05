package com.example.testypie.domain.comment.controller;

import com.example.testypie.domain.comment.dto.request.CreateCommentRequestDTO;
import com.example.testypie.domain.comment.dto.request.UpdateCommentRequestDTO;
import com.example.testypie.domain.comment.dto.response.CreateCommentResponseDTO;
import com.example.testypie.domain.comment.dto.response.ReadPageCommentResponseDTO;
import com.example.testypie.domain.comment.dto.response.UpdateCommentResponseDTO;
import com.example.testypie.domain.core.service.CommentMatcherService;
import com.example.testypie.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

  private final CommentMatcherService commentMatcherService;

  @PostMapping("/category/{parentCategory_name}/{childCategory_id}/products/{product_id}/comments")
  public ResponseEntity<CreateCommentResponseDTO> createComment(
      @PathVariable Long product_id,
      @Valid @RequestBody CreateCommentRequestDTO req,
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long childCategory_id,
      @PathVariable String parentCategory_name) {

    CreateCommentResponseDTO res =
        commentMatcherService.productComment(
            product_id, req, userDetails.getUser(), childCategory_id, parentCategory_name);
    return ResponseEntity.ok(res);
  }

  @GetMapping("/category/{parentCategory_name}/{childCategory_id}/products/{product_id}/comments")
  public ResponseEntity<Page<ReadPageCommentResponseDTO>> getComments(
      @PageableDefault(page = 1, sort = "comment_id", direction = Direction.DESC) Pageable pageable,
      @PathVariable Long product_id,
      @PathVariable Long childCategory_id,
      @PathVariable String parentCategory_name) {

    Page<ReadPageCommentResponseDTO> resList =
        commentMatcherService.getComments(
            pageable, product_id, childCategory_id, parentCategory_name);

    return ResponseEntity.ok().body(resList);
  }

  @PatchMapping(
      "/category/{parentCategory_name}/{childCategory_id}/products/{product_id}/comments/{comment_id}")
  public ResponseEntity<UpdateCommentResponseDTO> updateComment(
      @PathVariable Long product_id,
      @PathVariable Long comment_id,
      @Valid @RequestBody UpdateCommentRequestDTO req,
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long childCategory_id,
      @PathVariable String parentCategory_name) {

    UpdateCommentResponseDTO res =
        commentMatcherService.updateComment(
            product_id,
            comment_id,
            req,
            userDetails.getUser(),
            childCategory_id,
            parentCategory_name);
    return ResponseEntity.ok(res);
  }

  @DeleteMapping(
      "/category/{parentCategory_name}/{childCategory_id}/products/{product_id}/comments/{comment_id}")
  public ResponseEntity<Void> deleteComment(
      @PathVariable Long product_id,
      @PathVariable Long comment_id,
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long childCategory_id,
      @PathVariable String parentCategory_name) {

    commentMatcherService.deleteComment(
        product_id, comment_id, userDetails.getUser(), childCategory_id, parentCategory_name);
    return ResponseEntity.noContent().build();
  }
}
