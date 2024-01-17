package com.example.testypie.domain.reward.dto;

import com.example.testypie.domain.reward.entity.Reward;

public record RewardCreateResponseDTO(
        Long id,
        String reward_item,
        Long item_size
) {
        public static RewardCreateResponseDTO of(Reward reward) {
            return new RewardCreateResponseDTO(
                    reward.getId(),
                    reward.getReward_item(),
                    reward.getItem_size()
            );
        }
}
