package com.example.testypie.product.dto;

import java.time.LocalDateTime;

public record ProductCreateRequestDTO (

        String title,
        String content,
        String category,
        LocalDateTime startAt,
        LocalDateTime closedAt
) {
}
