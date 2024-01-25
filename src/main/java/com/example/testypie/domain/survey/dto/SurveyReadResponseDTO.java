package com.example.testypie.domain.survey.dto;

import com.example.testypie.domain.question.dto.QuestionReadResponseDTO;
import com.example.testypie.domain.survey.entity.Survey;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record SurveyReadResponseDTO (
        Long id,
        String title,
        LocalDateTime createAt,
        List<QuestionReadResponseDTO> questionList
) {
    public static SurveyReadResponseDTO of(Survey survey) {

        List<QuestionReadResponseDTO> questions = survey.getQuestionList().stream()
                .map(QuestionReadResponseDTO::of)
                .collect(Collectors.toList());

        return new SurveyReadResponseDTO(
                survey.getId(),
                survey.getTitle(),
                survey.getCreatedAt(),
                questions
        );
    }
}
