package com.example.testypie.domain.product.dto;

import java.time.LocalDateTime;

public record ProductUpdateRequestDTO(

        String title,
        String content,
        LocalDateTime startAt,
        LocalDateTime closedAt
) {
}
