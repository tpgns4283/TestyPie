package com.example.testypie.domain.product.dto.request;

import com.example.testypie.domain.reward.dto.request.CreateRewardRequestDTO;
import java.util.List;
import lombok.NonNull;

public record CreateProductRequestDTO(
    @NonNull String title,
    @NonNull String content,
    @NonNull List<CreateRewardRequestDTO> rewardList,
    @NonNull String startAt,
    @NonNull String closedAt) {}
