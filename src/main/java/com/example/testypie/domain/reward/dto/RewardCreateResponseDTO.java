package com.example.testypie.domain.reward.dto;

import com.example.testypie.domain.reward.entity.Reward;

public record RewardCreateResponseDTO(Long id, String rewardItem, Long itemSize) {
  public static RewardCreateResponseDTO of(Reward reward) {
    return new RewardCreateResponseDTO(
        reward.getId(), reward.getRewardItem(), reward.getItemSize());
  }
}
