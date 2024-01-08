package com.example.testypie.product.dto;

import java.time.LocalDateTime;

public record ProductRequestDTO (
        String title,
        String content,
        LocalDateTime startAt,
        LocalDateTime closedAt
) {
}
