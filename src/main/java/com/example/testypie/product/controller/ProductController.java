package com.example.testypie.product.controller;

import com.example.testypie.product.dto.ProductCreateRequestDTO;
import com.example.testypie.product.dto.ProductCreateResponseDTO;

import com.example.testypie.product.service.ProductService;
import com.example.testypie.security.UserDetailsImpl;
import com.example.testypie.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/product")
    public ResponseEntity<ProductCreateResponseDTO> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody ProductCreateRequestDTO req) {
        User user = userDetails.getUser();
        ProductCreateResponseDTO res = productService.createPost(user, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
