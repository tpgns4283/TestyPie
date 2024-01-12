package com.example.testypie.domain.product.dto;

import com.example.testypie.domain.product.entity.Product;


public record ProductDeleteResponseDTO (
        Long id
) {
    public static ProductDeleteResponseDTO of(Product product) {
        return new ProductDeleteResponseDTO(
                product.getId()
        );
    }
}
