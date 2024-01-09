package com.example.testypie.category.controller;

import com.example.testypie.category.dto.CategoryCreateRequestDTO;
import com.example.testypie.category.dto.CategoryCreateResponseDTO;
import com.example.testypie.category.service.CategoryService;
import com.example.testypie.security.UserDetailsImpl;
import com.example.testypie.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/category")
@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //Category 생성(관리자)
    @PostMapping
    public ResponseEntity<CategoryCreateResponseDTO> createCategory(@Valid @RequestBody CategoryCreateRequestDTO req) {
        CategoryCreateResponseDTO res = categoryService.createCategory(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
