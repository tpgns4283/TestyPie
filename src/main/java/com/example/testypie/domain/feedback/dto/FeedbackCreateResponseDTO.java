package com.example.testypie.domain.feedback.dto;

import com.example.testypie.domain.feedback.entity.Feedback;
import com.example.testypie.domain.survey.dto.OptionReadResponseDTO;
import com.example.testypie.domain.survey.dto.QuestionReadResponseDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record FeedbackCreateResponseDTO(
        Long id,
        LocalDateTime createdAt,
        Long productId,
        Long userId
    ) {
    public FeedbackCreateResponseDTO (Feedback feedback) {

        this(
                feedback.getId(),
                feedback.getCreatedAt(),
                feedback.getProduct().getId(),
                feedback.getUser().getId()
        );
    }
}
