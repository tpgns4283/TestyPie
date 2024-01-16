package com.example.testypie.domain.product.dto;

import java.time.LocalDateTime;

public record ProductCreateRequestDTO (

        String title,
        String content,
        LocalDateTime startAt,
        LocalDateTime closedAt
) {
}
