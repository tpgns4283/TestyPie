package com.example.testypie.domain.product.dto;

import lombok.NonNull;

import java.time.LocalDateTime;

public record ProductUpdateRequestDTO(

        @NonNull
        String title,
        @NonNull
        String content,
        @NonNull
        LocalDateTime startAt,
        @NonNull
        LocalDateTime closedAt
) {
}
