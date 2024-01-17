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

    public Category getCategory(Long categoryId, String parentCategory_name) {
        Category childCategory =  categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        Category parentCategory = categoryRepository.findByName(parentCategory_name);

        if (childCategory.getParent() == parentCategory){
            return childCategory;
        } else {
            throw new IllegalArgumentException("부모카테고리와 자식카테고리가 일치하지 않습니다.");
        }
    }
}
