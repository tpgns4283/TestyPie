package com.example.testypie.domain.productLike.controller;

import com.example.testypie.domain.productLike.dto.ProductLikeResponseDto;
import com.example.testypie.domain.productLike.service.ProductLikeService;
import com.example.testypie.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category/{parentCategory_name}/{childCategory_id}/products")
public class ProductLikeController {

  private final ProductLikeService productLikeService;

  @PatchMapping("/{product_id}/product_like")
  public ResponseEntity<ProductLikeResponseDto> clickProductLike(
      @PathVariable String parentCategory_name,
      @PathVariable Long childCategory_id,
      @PathVariable Long product_id,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    ProductLikeResponseDto res =
        productLikeService.clickProductLike(product_id, userDetails.getUser());
    return ResponseEntity.ok().body(res);
  }

  @GetMapping("/{product_id}/product_like/status")
  public ResponseEntity<ProductLikeResponseDto> getProductLike(
      @PathVariable String parentCategory_name,
      @PathVariable Long childCategory_id,
      @PathVariable Long product_id,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    ProductLikeResponseDto res =
        productLikeService.getProductLike(product_id, userDetails.getUser());
    return ResponseEntity.ok().body(res);
  }
}
