package com.example.testypie.domain.feedback.dto;

import com.example.testypie.domain.feedback.entity.Feedback;
import java.time.LocalDateTime;

public record FeedbackCreateResponseDTO(
    Long id, LocalDateTime createdAt, Long productId, Long userId) {
  public FeedbackCreateResponseDTO(Feedback feedback) {

    this(
        feedback.getId(),
        feedback.getCreatedAt(),
        feedback.getProduct().getId(),
        feedback.getUser().getId());
  }
}
