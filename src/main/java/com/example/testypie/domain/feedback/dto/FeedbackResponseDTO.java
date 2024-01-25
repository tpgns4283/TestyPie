package com.example.testypie.domain.feedback.dto;

import com.example.testypie.domain.feedback.entity.Feedback;
import java.time.LocalDateTime;

public record FeedbackResponseDTO(
    Long id,
    String title,
    LocalDateTime createdAt,
    Long productId
    ) {
        public FeedbackResponseDTO (Feedback feedback) {
            this(
                    feedback.getId(),
                    feedback.getTitle(),
                    feedback.getCreatedAt(),
                    feedback.getProduct().getId()
            );
        }
}
