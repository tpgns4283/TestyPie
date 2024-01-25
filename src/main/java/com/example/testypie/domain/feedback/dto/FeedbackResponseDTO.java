package com.example.testypie.domain.feedback.dto;

import com.example.testypie.domain.feedback.entity.Feedback;
import java.time.LocalDateTime;

public record FeedbackResponseDTO(
        Long id,
        String response
    ) {
}
