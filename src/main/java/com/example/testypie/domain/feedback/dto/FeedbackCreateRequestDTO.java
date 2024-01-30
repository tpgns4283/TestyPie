package com.example.testypie.domain.feedback.dto;

import lombok.NonNull;

import java.util.List;

public record FeedbackCreateRequestDTO (
        @NonNull
        List<FeedbackDetailsCreateRequestDTO> feedbackDetailsList
) {

}
