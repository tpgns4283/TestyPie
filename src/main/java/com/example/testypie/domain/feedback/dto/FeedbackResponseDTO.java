package com.example.testypie.domain.feedback.dto;

import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.user.entity.User;
import com.example.testypie.domain.feedback.entity.Feedback;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public record FeedbackResponseDTO(
    Long id,
    String title,
    LocalDateTime createdAt,
    User user,
    Product product
    ) {
        public FeedbackResponseDTO (Feedback saveFeedback) {
            this(
                saveFeedback.getId(),
                saveFeedback.getTitle(),
                saveFeedback.getCreatedAt(),
                saveFeedback.getUser(),
                saveFeedback.getProduct()
            );
        }
}
