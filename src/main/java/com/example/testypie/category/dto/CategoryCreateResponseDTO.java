package com.example.testypie.category.dto;

import com.example.testypie.category.entity.Category;

import java.util.List;


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
