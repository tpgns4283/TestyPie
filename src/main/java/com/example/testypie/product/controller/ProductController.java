package com.example.testypie.product.controller;

import com.example.testypie.product.dto.*;

import com.example.testypie.product.service.ProductService;
import com.example.testypie.security.UserDetailsImpl;
import com.example.testypie.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductCreateResponseDTO> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody ProductCreateRequestDTO req) {
        User user = userDetails.getUser();
        ProductCreateResponseDTO res = productService.createPost(user, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductReadResponseDTO> getProduct(@PathVariable Long productId) {
        ProductReadResponseDTO res = productService.getProduct(productId);
        return ResponseEntity.ok().body(res);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductUpdateResponseDTO> updateProduct(@PathVariable Long productId,
                                                                  @RequestBody ProductUpdateRequestDTO req,
                                                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ProductUpdateResponseDTO res = productService.updateProduct(productId, req, userDetails.getUser());
        return ResponseEntity.ok().body(res);
    }
}
