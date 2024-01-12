package com.example.testypie.domain.category.service;

import com.example.testypie.domain.category.dto.CategoryCreateRequestDTO;
import com.example.testypie.domain.category.dto.CategoryCreateResponseDTO;
import com.example.testypie.domain.category.entity.Category;
import com.example.testypie.domain.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryCreateResponseDTO createCategory(CategoryCreateRequestDTO req) {
        Category parentName = categoryRepository.findByName(req.parent());
            Category category = Category.builder().name(req.name()).depth((long) 1).parent(parentName).build();
            Category saveCategory = categoryRepository.save(category);
            return CategoryCreateResponseDTO.of(saveCategory);
    }
}
