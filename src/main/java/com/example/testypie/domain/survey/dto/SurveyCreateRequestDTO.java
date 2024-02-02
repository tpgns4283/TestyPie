package com.example.testypie.domain.survey.dto;

import java.util.List;
import lombok.NonNull;

public record SurveyCreateRequestDTO(
    @NonNull String title, @NonNull List<QuestionCreateRequestDTO> questionList) {}
