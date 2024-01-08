package com.example.testypie.product.dto;

import java.time.LocalDateTime;

public record ProductResponseDTO (
        Long id,
        String title,
        String content,
        LocalDateTime createAt,
        LocalDateTime startAt,
        LocalDateTime closedAt
) {
}
