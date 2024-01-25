package com.example.testypie.domain.question.dto;

import com.example.testypie.domain.option.dto.OptionCreateRequestDTO;
import com.example.testypie.domain.question.entity.QuestionType;

import java.util.List;

public record QuestionCreateRequestDTO(
        String text,
        QuestionType questionType,
        List<OptionCreateRequestDTO> optionList
) {

}
