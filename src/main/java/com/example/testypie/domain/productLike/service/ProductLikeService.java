package com.example.testypie.domain.productLike.service;

import static com.example.testypie.domain.product.constant.ProductConstant.DEFAULT_PRODUCT_LIKE;

import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.product.service.ProductService;
import com.example.testypie.domain.productLike.Repository.ProductLikeRepository;
import com.example.testypie.domain.productLike.dto.ProductLikeResponseDto;
import com.example.testypie.domain.productLike.entity.ProductLike;
import com.example.testypie.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductLikeService {

    private final ProductLikeRepository productLikeRepository;
    private final ProductService productService;

    @Transactional
    public ProductLikeResponseDto clickProductLike(Long productId, User user) {

        Product product = productService.findProduct(productId);
        ProductLike productLike = productLikeRepository.findByUserAndProduct(product, user)
                .orElseGet(() -> saveProductLike(user, product));

        boolean clickProductLike = productLike.clickProductLike();
        product.updateProductLikeCnt(clickProductLike);

        return ProductLikeResponseDto.of(productLike.getIsProductLiked());
    }

    private ProductLike saveProductLike(User user, Product product) {

        ProductLike productLike = ProductLike.builder()
                .user(user)
                .product(product)
                .isProductLiked(DEFAULT_PRODUCT_LIKE)
                .build();

        return productLikeRepository.save(productLike);
    }
}
