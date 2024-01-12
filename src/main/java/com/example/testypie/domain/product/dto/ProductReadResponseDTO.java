package com.example.testypie.domain.product.dto;

import com.example.testypie.domain.product.entity.Product;

import java.time.LocalDateTime;

public record ProductReadResponseDTO (
        Long id,
        String title,
        String content,
        String category,
        LocalDateTime createAt,
        LocalDateTime startAt,
        LocalDateTime closedAt
) {

    public static ProductReadResponseDTO of(Product product) {
        return new ProductReadResponseDTO(
                product.getId(),
                product.getTitle(),
                product.getContent(),
                product.getCategory().getName(),
                product.getCreateAt(),
                product.getStartedAt(),
                product.getClosedAt()
        );
    }
}