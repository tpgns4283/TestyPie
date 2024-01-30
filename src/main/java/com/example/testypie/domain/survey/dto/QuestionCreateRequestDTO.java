package com.example.testypie.domain.survey.dto;

import com.example.testypie.domain.survey.entity.QuestionType;
import lombok.NonNull;

import java.util.List;

public record QuestionCreateRequestDTO(
        @NonNull
        String text,
        @NonNull
        QuestionType questionType,
        List<OptionCreateRequestDTO> optionList
) {

}
