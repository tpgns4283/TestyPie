package com.example.testypie.domain.feedback.dto;

import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.question.dto.QuestionCreateRequestDTO;
import com.example.testypie.domain.user.entity.User;
import com.example.testypie.domain.feedback.entity.Feedback;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.List;

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
