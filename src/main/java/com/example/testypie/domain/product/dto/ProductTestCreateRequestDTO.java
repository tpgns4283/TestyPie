package com.example.testypie.domain.product.dto;

import com.example.testypie.domain.reward.dto.RewardCreateRequestDTO;
import java.util.List;

public record ProductTestCreateRequestDTO(
    List<RewardCreateRequestDTO> rewardList,
    String startAt,
    String closedAt) {}
