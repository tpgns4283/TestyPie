package com.example.testypie.domain.product.dto;

import com.example.testypie.domain.reward.dto.RewardCreateRequestDTO;

import java.time.LocalDateTime;
import java.util.List;

public record ProductCreateRequestDTO (
        String title,
        String content,
        List<RewardCreateRequestDTO> rewardList,
        LocalDateTime startAt,
        LocalDateTime closedAt
) {
}
