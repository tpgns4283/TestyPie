package com.example.testypie.domain.question.dto;

import com.example.testypie.domain.feedback.entity.Feedback;
import com.example.testypie.domain.question.entity.Question;
import com.example.testypie.domain.question.entity.QuestionType;

import java.util.List;
import java.util.stream.Collectors;

public class QuestionMapper {
    public static List<Question> mapToEntityList(List<QuestionCreateRequestDTO> dtoList, Feedback feedback) {
        return dtoList.stream()
                .map(dto -> mapToEntity(dto, feedback))
                .collect(Collectors.toList());
    }

    public static Question mapToEntity(QuestionCreateRequestDTO dto, Feedback feedback) {
        return Question.builder()
                .text(dto.text())
                .questionType(dto.questionType())
                .feedback(feedback)
                .build();
    }
}