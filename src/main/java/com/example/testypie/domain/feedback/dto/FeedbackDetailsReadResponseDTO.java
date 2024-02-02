package com.example.testypie.domain.feedback.dto;

import com.example.testypie.domain.feedback.entity.FeedbackDetails;

public record FeedbackDetailsReadResponseDTO(Long id, String response, Long feedbackId) {
    public static FeedbackDetailsReadResponseDTO of(FeedbackDetails feedbackDetails) {

        return new FeedbackDetailsReadResponseDTO(
                feedbackDetails.getId(),
                feedbackDetails.getResponse(),
                feedbackDetails.getFeedback().getId());
    }
}
