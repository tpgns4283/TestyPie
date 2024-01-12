package com.example.testypie.domain.user.dto;

import java.time.LocalDateTime;

public record RegisteredProductResponseDTO(
        Long id,
        String title,
        LocalDateTime createdAt,
        LocalDateTime closedAt
) {
}
