package com.example.testypie.domain.category.controller;

import com.example.testypie.domain.category.dto.CategoryCreateRequestDTO;
import com.example.testypie.domain.category.dto.CategoryCreateResponseDTO;
import com.example.testypie.domain.category.dto.CategoryReadResponseDTO;
import com.example.testypie.domain.category.entity.Category;
import com.example.testypie.domain.category.repository.CategoryRepository;
import com.example.testypie.domain.category.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/categories")
@RestController
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryController(CategoryService categoryService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    //Category 생성(관리자 권한 추가 예정 / UserRole == Admin)
    @PostMapping
    public ResponseEntity<CategoryCreateResponseDTO> createCategory(@Valid @RequestBody CategoryCreateRequestDTO req) {
        CategoryCreateResponseDTO res = categoryService.createCategory(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping
    public List<CategoryReadResponseDTO> getCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryReadResponseDTO::of)
                .collect(Collectors.toList());
    }
}
