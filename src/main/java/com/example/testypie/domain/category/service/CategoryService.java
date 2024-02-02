package com.example.testypie.domain.category.service;

import com.example.testypie.domain.category.dto.CategoryCreateRequestDTO;
import com.example.testypie.domain.category.dto.CategoryCreateResponseDTO;
import com.example.testypie.domain.category.entity.Category;
import com.example.testypie.domain.category.repository.CategoryRepository;
import com.example.testypie.global.exception.ErrorCode;
import com.example.testypie.global.exception.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryCreateResponseDTO createCategory(CategoryCreateRequestDTO req) {
        Category parentName = categoryRepository.findByName(req.parent());
        Category category =
                Category.builder().name(req.name()).depth((long) 1).parent(parentName).build();
        Category saveCategory = categoryRepository.save(category);
        return CategoryCreateResponseDTO.of(saveCategory);
    }

    public Category getCategory(Long categoryId, String parentCategory_name) {
        Category childCategory =
                categoryRepository
                        .findById(categoryId)
                        .orElseThrow(
                                () ->
                                        new GlobalExceptionHandler.CustomException(
                                                ErrorCode.SELECT_CATEGORY_NOT_FOUND));

        Category parentCategory = categoryRepository.findByName(parentCategory_name);

        if (childCategory.getParent() == parentCategory) {
            return childCategory;
        } else {
            throw new GlobalExceptionHandler.CustomException(ErrorCode.SELECT_CATEGORY_NOT_MATCH);
        }
    }
}
