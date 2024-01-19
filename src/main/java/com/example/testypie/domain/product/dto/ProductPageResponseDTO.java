package com.example.testypie.domain.product.dto;

import com.example.testypie.domain.product.entity.Product;
import java.time.LocalDateTime;


public record ProductPageResponseDTO (
        Long id,
        String account,
        String title,
        String content,
        String category,
        LocalDateTime createAt,
        LocalDateTime startAt,
        LocalDateTime closedAt

) {
    public static ProductPageResponseDTO of(Product product) {
        return new ProductPageResponseDTO(
                product.getId(),
                product.getUser().getAccount(),
                product.getTitle(),
                product.getContent(),
                product.getCategory().getName(),
                product.getCreatedAt(),
                product.getStartedAt(),
                product.getClosedAt()
        );
    }
}