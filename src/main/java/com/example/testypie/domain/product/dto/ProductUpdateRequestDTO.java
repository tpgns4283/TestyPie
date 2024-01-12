package com.example.testypie.domain.product.dto;

import java.time.LocalDateTime;

public record ProductUpdateRequestDTO(

        String title,
        String content,
        String category,
        LocalDateTime startAt,
        LocalDateTime closedAt
) {
}
