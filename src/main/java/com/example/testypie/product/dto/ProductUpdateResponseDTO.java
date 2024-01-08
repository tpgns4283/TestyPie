package com.example.testypie.product.dto;

import com.example.testypie.product.entity.Product;

import java.time.LocalDateTime;

public record ProductUpdateResponseDTO(
        Long id,
        String title,
        String content,
        LocalDateTime modifiedAt,
        LocalDateTime startAt,
        LocalDateTime closedAt
) {
    public static ProductUpdateResponseDTO of(Product product) {
        return new ProductUpdateResponseDTO(
                product.getId(),
                product.getTitle(),
                product.getContent(),
                product.getModifiedAt(),
                product.getStartedAt(),
                product.getClosedAt()
        );
    }
}
