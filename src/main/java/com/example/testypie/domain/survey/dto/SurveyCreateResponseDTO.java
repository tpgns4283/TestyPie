package com.example.testypie.domain.survey.dto;

import com.example.testypie.domain.survey.entity.Survey;
import java.time.LocalDateTime;

public record SurveyCreateResponseDTO(
    Long id, String title, LocalDateTime createdAt, Long productId) {
  public SurveyCreateResponseDTO(Survey survey) {
    this(survey.getId(), survey.getTitle(), survey.getCreatedAt(), survey.getProduct().getId());
  }
}
