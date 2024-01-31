package com.example.testypie.domain.product.dto;

import com.example.testypie.domain.reward.dto.RewardCreateRequestDTO;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;

public record ProductCreateRequestDTO (
        @NonNull
        String title,
        @NonNull
        String content,
        @NonNull
        List<RewardCreateRequestDTO> rewardList,
        @NonNull
        String startAt,
        @NonNull
        String closedAt
) {
}
