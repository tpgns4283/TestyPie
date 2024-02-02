package com.example.testypie.domain.feedback.controller;

import com.example.testypie.domain.feedback.dto.FeedbackCreateRequestDTO;
import com.example.testypie.domain.feedback.dto.FeedbackCreateResponseDTO;
import com.example.testypie.domain.feedback.service.FeedbackService;
import com.example.testypie.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FeedbackController {
  private final FeedbackService feedbackService;

  @PostMapping("/category/{parentCategoryName}/{childCategoryId}/products/{productId}/feedback")
  public ResponseEntity<FeedbackCreateResponseDTO> addFeedback(
      @RequestBody FeedbackCreateRequestDTO req,
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long productId,
      @PathVariable Long childCategoryId,
      @PathVariable String parentCategoryName) {
    FeedbackCreateResponseDTO res =
        feedbackService.addFeedback(
            req, productId, userDetails.getUser(), childCategoryId, parentCategoryName);
    return ResponseEntity.status(HttpStatus.CREATED).body(res);
  }
}
