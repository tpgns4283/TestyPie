package com.example.testypie.domain.product.dto;

import com.example.testypie.domain.product.entity.Product;
import java.time.LocalDateTime;


public record ProductPageResponseDTO (
        Long id,
        String account,
        String title,
        String content,
        Long childCategoryId,
        String parentCategoryName,
        Long productLikeCnt,
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
                product.getCategory().getId(),
                product.getCategory().getParent().getName(),
                product.getProductLikeCnt(),
                product.getCreatedAt(),
                product.getStartedAt(),
                product.getClosedAt()
        );
    }
}