package com.example.testypie.domain.feedback.dto;

import java.util.List;

public record FeedbackCreateRequestDTO (
        List<FeedbackDetailsCreateRequestDTO> feedbackDetailsList
) {

}
