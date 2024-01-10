package com.example.testypie.product.controller;

import com.example.testypie.product.dto.*;

import com.example.testypie.product.service.ProductService;
import com.example.testypie.security.UserDetailsImpl;
import com.example.testypie.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //Product 생성
    @PostMapping
    public ResponseEntity<ProductCreateResponseDTO> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody ProductCreateRequestDTO req) {
        User user = userDetails.getUser();
        ProductCreateResponseDTO res = productService.createPost(user, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    //Product 단일 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ProductReadResponseDTO> getProduct(@PathVariable Long productId) {
        ProductReadResponseDTO res = productService.getProduct(productId);
        return ResponseEntity.ok().body(res);
    }

    //Product 전체 조회(페이징)
    @GetMapping
    public ResponseEntity<Page<ProductReadResponseDTO>> getProductPage(
            // (page = 1) => 1페이지부터 시작
            @PageableDefault(page = 1) Pageable pageable) {
        Page<ProductReadResponseDTO> res = productService.getProductPage(pageable);
        return ResponseEntity.ok().body(res);
    }

    //Product 수정
    @PatchMapping("/{productId}")
    public ResponseEntity<ProductUpdateResponseDTO> updateProduct(@PathVariable Long productId,
                                                                  @RequestBody ProductUpdateRequestDTO req,
                                                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProductUpdateResponseDTO res = productService.updateProduct(productId, req, userDetails.getUser());
        return ResponseEntity.ok().body(res);
    }

    //Product 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<ProductDeleteResponseDTO> deleteProduct(@PathVariable Long productId,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProductDeleteResponseDTO res = productService.deleteProduct(productId, userDetails.getUser());
        return ResponseEntity.ok().body(res);
    }
}
