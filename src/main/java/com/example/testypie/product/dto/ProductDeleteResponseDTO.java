package com.example.testypie.product.dto;

import com.example.testypie.product.entity.Product;


public record ProductDeleteResponseDTO (
        Long id
) {
    public static ProductDeleteResponseDTO of(Product product) {
        return new ProductDeleteResponseDTO(
                product.getId()
        );
    }
}
