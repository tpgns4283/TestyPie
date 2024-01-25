package com.example.testypie.domain.question.dto;

import com.example.testypie.domain.option.dto.OptionReadResponseDTO;
import com.example.testypie.domain.option.entity.Option;
import com.example.testypie.domain.question.entity.Question;
import com.example.testypie.domain.question.entity.QuestionType;

import java.util.List;
import java.util.stream.Collectors;

public record QuestionReadResponseDTO(
        Long id,
        String text,
        QuestionType questionType,
        List<OptionReadResponseDTO> optionList,
        Long surveyId
) {

    public static QuestionReadResponseDTO of(Question question) {
        List<OptionReadResponseDTO> optionList = question.getOptionList().stream()
                .map(OptionReadResponseDTO::of)
                .collect(Collectors.toList());
        return new QuestionReadResponseDTO(
                question.getId(),
                question.getText(),
                question.getQuestionType(),
                optionList,
                question.getSurvey().getId()

        );
    }
}
