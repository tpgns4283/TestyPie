package com.example.testypie.domain.survey.dto;

import com.example.testypie.domain.question.dto.QuestionCreateRequestDTO;

import java.util.List;

public record SurveyCreateRequestDTO(
        String title,
        String content,
        List<QuestionCreateRequestDTO> questionList
) {
}
