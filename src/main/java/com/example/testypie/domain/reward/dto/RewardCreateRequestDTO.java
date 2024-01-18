package com.example.testypie.domain.reward.dto;

public record RewardCreateRequestDTO(
        String rewardItem,
        Long itemSize
) {
}
