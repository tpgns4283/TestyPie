package com.example.testypie.domain.reward.dto;

public record RewardCreateRequestDTO(
        String reward_item,
        Long item_size
) {
}
