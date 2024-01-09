package com.example.testypie.product.dto;

import com.example.testypie.product.entity.Product;

import java.time.LocalDateTime;

public record ProductReadResponseDTO (
        Long id,
        String title,
        String content,
        LocalDateTime createAt,
        LocalDateTime startAt,
        LocalDateTime closedAt
) {
    public static ProductReadResponseDTO of(Product product) {
        return new ProductReadResponseDTO(
                product.getId(),
                product.getTitle(),
                product.getContent(),
                product.getCreateAt(),
                product.getStartedAt(),
                product.getClosedAt()
        );
    }
}