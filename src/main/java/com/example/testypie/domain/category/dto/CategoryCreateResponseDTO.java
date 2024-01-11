package com.example.testypie.domain.category.dto;

import com.example.testypie.domain.category.entity.Category;

public record CategoryCreateResponseDTO(
        Long id,
        String name,
        Long depth,
        Category parent
) {

    public static CategoryCreateResponseDTO of(Category category) {
        return new CategoryCreateResponseDTO(
                category.getId(),
                category.getName(),
                category.getDepth(),
                category.getParent()
        );
    }
}
