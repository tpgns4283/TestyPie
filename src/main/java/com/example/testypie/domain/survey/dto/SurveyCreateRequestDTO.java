package com.example.testypie.domain.survey.dto;

import java.util.List;

public record SurveyCreateRequestDTO(
        String title,
        String content,
        List<QuestionCreateRequestDTO> questionList
) {
}
