package com.example.testypie.feedback.dto;

import com.example.testypie.feedback.entity.Feedback;
import com.example.testypie.product.entity.Product;
import com.example.testypie.user.entity.User;
import java.time.LocalDateTime;

public record FeedbackResponseDTO(
    Long id,
    Double grade,
    String title,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt,
    String content,
    User user,
    Product product
    ) {
        public FeedbackResponseDTO (Feedback saveFeedback) {
            this(
                saveFeedback.getId(),
                saveFeedback.getGrade(),
                saveFeedback.getTitle(),
                saveFeedback.getCreatedAt(),
                saveFeedback.getModifiedAt(),
                saveFeedback.getContent(),
                saveFeedback.getUser(),
                saveFeedback.getProduct()
            );
        }
}
