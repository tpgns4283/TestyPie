package com.example.testypie.domain.feedback.dto;

import java.util.List;
import lombok.NonNull;

public record FeedbackCreateRequestDTO(
        @NonNull List<FeedbackDetailsCreateRequestDTO> feedbackDetailsList) {}
