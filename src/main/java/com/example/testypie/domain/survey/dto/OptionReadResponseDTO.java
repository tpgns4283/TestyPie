package com.example.testypie.domain.survey.dto;

import com.example.testypie.domain.survey.entity.Option;

public record OptionReadResponseDTO(

        Long id,
        String text,
        Long questionId
) {

    public static OptionReadResponseDTO of(Option option) {
        return new OptionReadResponseDTO(
                option.getId(),
                option.getText(),
                option.getQuestion().getId()
        );
    }
}