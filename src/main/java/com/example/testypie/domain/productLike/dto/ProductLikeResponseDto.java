package com.example.testypie.domain.productLike.dto;

public record ProductLikeResponseDto(Boolean isProductLiked) {

    public static ProductLikeResponseDto of(Boolean isProductLiked) {

        return new ProductLikeResponseDto(isProductLiked);
    }
}
