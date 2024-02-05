package com.example.testypie.domain.reward.dto;

import jakarta.validation.constraints.Size;
import lombok.NonNull;

public record RewardCreateRequestDTO(@NonNull String rewardItem, @NonNull @Size Long itemSize) {}
