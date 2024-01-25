package com.example.testypie.domain.feedback.dto;

import com.example.testypie.domain.answer.dto.AnswerReadResponseDTO;
import com.example.testypie.domain.feedback.entity.Feedback;

import com.example.testypie.domain.question.dto.QuestionReadResponseDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record FeedbackReadResponseDTO (
        Long id,
        String title,
        LocalDateTime createAt,
        List<QuestionReadResponseDTO> questionList
) {
    public static FeedbackReadResponseDTO of(Feedback feedback) {

        List<QuestionReadResponseDTO> questions = feedback.getQuestionList().stream()
                .map(QuestionReadResponseDTO::of)
                .collect(Collectors.toList());

        return new FeedbackReadResponseDTO(
                feedback.getId(),
                feedback.getTitle(),
                feedback.getCreatedAt(),
                questions
        );
    }
}