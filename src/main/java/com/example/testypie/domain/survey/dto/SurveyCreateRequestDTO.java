package com.example.testypie.domain.survey.dto;

import lombok.NonNull;

import java.util.List;

public record SurveyCreateRequestDTO(
        @NonNull
        String title,
        @NonNull
        List<QuestionCreateRequestDTO> questionList
) {
}
