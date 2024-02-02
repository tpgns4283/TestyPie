package com.example.testypie.domain.reward.dto;

import lombok.NonNull;

public record RewardCreateRequestDTO(@NonNull String rewardItem, @NonNull Long itemSize) {}
