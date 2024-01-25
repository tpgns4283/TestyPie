package com.example.testypie.domain.feedback.dto;

import com.example.testypie.domain.question.dto.QuestionCreateRequestDTO;

import java.util.List;

public record FeedbackCreateRequestDTO (
        String title,
        String content,
        List<QuestionCreateRequestDTO> questionList
) {

}
