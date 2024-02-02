package com.example.testypie.domain.product.dto;

import java.time.LocalDateTime;
import lombok.NonNull;

public record ProductUpdateRequestDTO(
        @NonNull String title,
        @NonNull String content,
        @NonNull LocalDateTime startAt,
        @NonNull LocalDateTime closedAt) {}
