package com.example.testypie.domain.product.dto;

import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.reward.dto.RewardCreateRequestDTO;
import com.example.testypie.domain.reward.dto.RewardReadResponseDTO;
import com.example.testypie.domain.reward.entity.Reward;
import com.example.testypie.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public record ProductCreateResponseDTO (
        Long id,
        String title,
        String content,
        String category,
        LocalDateTime createAt,
        LocalDateTime startAt,
        LocalDateTime closedAt
) {

    public static ProductCreateResponseDTO of(Product product) {

        return new ProductCreateResponseDTO(
                product.getId(),
                product.getTitle(),
                product.getContent(),
                product.getCategory().getName(),
                product.getCreatedAt(),
                product.getStartedAt(),
                product.getClosedAt()
        );
    }
}
