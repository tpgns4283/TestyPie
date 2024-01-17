package com.example.testypie.domain.reward.dto;

import com.example.testypie.domain.product.entity.Product;

public record RewardCreateRequestDTO(
        String reward_item,
        Long item_size,
        Product product
) {
}
