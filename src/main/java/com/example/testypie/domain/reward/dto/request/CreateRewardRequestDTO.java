package com.example.testypie.domain.reward.dto.request;

import jakarta.validation.constraints.Size;
import lombok.NonNull;

public record CreateRewardRequestDTO(@NonNull String rewardItem, @NonNull @Size Long itemSize) {}
