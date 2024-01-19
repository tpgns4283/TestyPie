package com.example.testypie.domain.product.controller;
import com.example.testypie.domain.product.dto.*;

import com.example.testypie.domain.product.service.ProductService;
import com.example.testypie.global.security.UserDetailsImpl;
import com.example.testypie.domain.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //Product 생성
    @PostMapping("/category/{parentCategory_name}/{childCategory_id}/products")
    public ResponseEntity<ProductCreateResponseDTO> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                               @Valid @RequestBody ProductCreateRequestDTO req,
                                                               @PathVariable Long childCategory_id,
                                                               @PathVariable String parentCategory_name) {

        User user = userDetails.getUser();
        ProductCreateResponseDTO res = productService.createProduct(user, req, parentCategory_name,childCategory_id);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    //Product 단일 조회
    @GetMapping("/category/{parentCategory_name}/{childCategory_id}/products/{productId}")
    public ResponseEntity<ProductReadResponseDTO> getProduct(@PathVariable Long productId,
                                                             @PathVariable Long childCategory_id,
                                                             @PathVariable String parentCategory_name) throws ParseException {
        ProductReadResponseDTO res = productService.getProduct(productId, childCategory_id, parentCategory_name);
        return ResponseEntity.ok().body(res);
    }

    //Product 전체 조회 및 카테고리 조회(페이징)
    @GetMapping(value = {"/category/{parentCategory_name}", "/category/{parentCategory_name}/{childCategory_id}"})
    public ResponseEntity<Page<ProductPageResponseDTO>> getProductPage(
            // (page = 1) => 1페이지부터 시작
            @PageableDefault(page = 1) Pageable pageable,
            @PathVariable(required = false) Long childCategory_id,
            @PathVariable String parentCategory_name) throws ParseException {
        Page<ProductPageResponseDTO> res;
        if(Objects.isNull(childCategory_id)){
            res = productService.getProductPage(pageable, parentCategory_name);
        } else {
            res = productService.getProductCategoryPage(pageable, childCategory_id, parentCategory_name);
        }
        return ResponseEntity.ok().body(res);
    }

    //Product 수정
    @PatchMapping("/category/{parentCategory_name}/{childCategory_id}/products/{productId}")
    public ResponseEntity<ProductUpdateResponseDTO> updateProduct(@PathVariable Long productId,
                                                                  @RequestBody ProductUpdateRequestDTO req,
                                                                  @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                  @PathVariable Long childCategory_id,
                                                                  @PathVariable String parentCategory_name) {
        ProductUpdateResponseDTO res = productService.updateProduct(productId, req, userDetails.getUser(), childCategory_id, parentCategory_name);
        return ResponseEntity.ok().body(res);
    }

    //Product 삭제
    @DeleteMapping("/category/{parentCategory_name}/{childCategory_id}/products/{productId}")
    public ResponseEntity<ProductDeleteResponseDTO> deleteProduct(@PathVariable Long productId,
                                                                  @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                  @PathVariable Long childCategory_id,
                                                                  @PathVariable String parentCategory_name) {
        ProductDeleteResponseDTO res = productService.deleteProduct(productId, userDetails.getUser(), childCategory_id, parentCategory_name);
        return ResponseEntity.ok().body(res);
    }
}
