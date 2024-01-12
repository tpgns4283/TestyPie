package com.example.testypie.domain.category.controller;

import com.example.testypie.domain.category.dto.CategoryCreateRequestDTO;
import com.example.testypie.domain.category.dto.CategoryCreateResponseDTO;
import com.example.testypie.domain.category.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/category")
@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //Category 생성(관리자 권한 추가 예정 / UserRole == Admin)
    @PostMapping
    public ResponseEntity<CategoryCreateResponseDTO> createCategory(@Valid @RequestBody CategoryCreateRequestDTO req) {
        CategoryCreateResponseDTO res = categoryService.createCategory(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
