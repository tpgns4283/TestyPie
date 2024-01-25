package com.example.testypie.domain.question.dto;

import com.example.testypie.domain.question.entity.QuestionType;

public record QuestionCreateRequestDTO(
        String text,
        QuestionType questionType
) {

}
