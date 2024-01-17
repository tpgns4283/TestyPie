package com.example.testypie.domain.reward.dto;

import com.example.testypie.domain.comment.entity.Comment;
import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.reward.entity.Reward;
import com.example.testypie.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public record RewardReadResponseDTO(
        Long id,
        String reward_item,
        Long item_size,
        User user,
        Product product
) {

    public RewardReadResponseDTO(Reward reward) {
        this(
                reward.getId(),
                reward.getReward_item(),
                reward.getItem_size(),
                reward.getUser(),
                reward.getProduct()
        );
    }
}
