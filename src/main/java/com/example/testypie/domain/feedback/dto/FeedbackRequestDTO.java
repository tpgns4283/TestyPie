package com.example.testypie.domain.feedback.dto;

public record FeedbackRequestDTO (

        Double grade,
        String title,
        String content
) {

}
