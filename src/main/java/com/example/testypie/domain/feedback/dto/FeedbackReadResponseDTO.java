package com.example.testypie.domain.feedback.dto;

import com.example.testypie.domain.feedback.entity.Feedback;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record FeedbackReadResponseDTO(
    Long id,
    LocalDateTime createdAt,
    List<FeedbackDetailsReadResponseDTO> feedbackDetailsList,
    Long userId,
    Long productId,
    Double rating) {
  public static FeedbackReadResponseDTO of(Feedback feedback) {

    List<FeedbackDetailsReadResponseDTO> detailsDTO =
        feedback.getFeedbackDetailsList().stream()
            .map(FeedbackDetailsReadResponseDTO::of)
            .collect(Collectors.toList());

    return new FeedbackReadResponseDTO(
        feedback.getId(),
        feedback.getCreatedAt(),
        detailsDTO,
        feedback.getUser().getId(),
        feedback.getProduct().getId(),
        feedback.getRating());
  }
}
