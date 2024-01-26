package com.example.testypie.domain.survey.dto;

import com.example.testypie.domain.survey.entity.QuestionType;

import java.util.List;

public record QuestionCreateRequestDTO(
        String text,
        QuestionType questionType,
        List<OptionCreateRequestDTO> optionList
) {

}
