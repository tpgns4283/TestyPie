package com.example.testypie.domain.reward.dto;

import jakarta.validation.constraints.Min;
import lombok.NonNull;

public record RewardCreateRequestDTO(@NonNull String rewardItem, @NonNull @Min(0) Long itemSize) {}
