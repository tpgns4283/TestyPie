package com.example.testypie.product.controller;

import com.example.testypie.product.dto.ProductRequestDTO;
import com.example.testypie.product.entity.Product;
import com.example.testypie.product.service.ProductService;
import com.example.testypie.security.UserDetailsImpl;
import com.example.testypie.user.entity.User;
import jakarta.validation.Valid;
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
    public Product createPost(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody ProductRequestDTO req) {
        User user = userDetails.getUser();
        return productService.createPost(user, req);
    }
}
