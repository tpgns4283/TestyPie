package com.example.testypie.domain.reward.dto;

import com.example.testypie.domain.reward.entity.Reward;
import com.example.testypie.domain.user.entity.User;

public record RewardReadResponseDTO(
        Long id, String rewardItem, Long itemSize, User user, Long productId) {

    public RewardReadResponseDTO(Reward reward) {
        this(
                reward.getId(),
                reward.getRewardItem(),
                reward.getItemSize(),
                reward.getUser(),
                reward.getProduct().getId());
    }
}
