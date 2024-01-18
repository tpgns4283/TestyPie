package com.example.testypie.domain.feedback.dto;

import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.user.entity.User;
import com.example.testypie.domain.feedback.entity.Feedback;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public record FeedbackResponseDTO(
    Long id,
    Double grade,
    String title,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt,
    String content,
    String bugReport,
    String bugImg,
    @JsonIgnore
    User user,
    @JsonIgnore
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
                saveFeedback.getBugReport(),
                saveFeedback.getBugImg(),
                saveFeedback.getUser(),
                saveFeedback.getProduct()
            );
        }
}
