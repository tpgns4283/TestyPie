package com.example.testypie.domain.survey.dto;

import lombok.NonNull;

public record OptionCreateRequestDTO(
        @NonNull
        String text
) {
}
