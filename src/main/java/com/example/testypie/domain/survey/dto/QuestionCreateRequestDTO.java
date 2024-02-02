package com.example.testypie.domain.survey.dto;

import com.example.testypie.domain.survey.entity.QuestionType;
import java.util.List;
import lombok.NonNull;

public record QuestionCreateRequestDTO(
    @NonNull String text,
    @NonNull QuestionType questionType,
    List<OptionCreateRequestDTO> optionList) {}
