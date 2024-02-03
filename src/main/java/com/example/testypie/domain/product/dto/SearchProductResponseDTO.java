package com.example.testypie.domain.product.dto;

import com.example.testypie.domain.product.entity.Product;
import java.time.LocalDateTime;

public record SearchProductResponseDTO(
    Long id,
    String account,
    String nickname,
    String title,
    String content,
    String parentCategoryName,
    Long childCategoryId,
    Long productLikeCnt,
    LocalDateTime createAt,
    LocalDateTime startAt,
    LocalDateTime closedAt) {

  public static SearchProductResponseDTO of(Product product) {
    return new SearchProductResponseDTO(
        product.getId(),
        product.getUser().getAccount(),
        product.getUser().getNickname(),
        product.getTitle(),
        product.getContent(),
        product.getCategory().getParent().getName(),
        product.getCategory().getId(),
        product.getProductLikeCnt(),
        product.getCreatedAt(),
        product.getStartedAt(),
        product.getClosedAt());
  }
}
