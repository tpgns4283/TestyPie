package com.example.testypie.domain.reward.dto;

import com.example.testypie.domain.reward.entity.Reward;

public record RewardDeleteResponseDTO(Long id) {
    public static RewardDeleteResponseDTO of(Reward reward) {
        return new RewardDeleteResponseDTO(reward.getId());
    }
}
