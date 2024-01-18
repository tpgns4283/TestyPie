package com.example.testypie.domain.reward.dto;

import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.reward.entity.Reward;

import java.util.List;
import java.util.stream.Collectors;

public class RewardMapper {
    public static List<Reward> mapToEntityList(List<RewardCreateRequestDTO> dtoList, Product product) {
        return dtoList.stream()
                .map(dto -> mapToEntity(dto, product))
                .collect(Collectors.toList());
    }

    public static Reward mapToEntity(RewardCreateRequestDTO dto, Product product) {
        Reward reward = new Reward();
        reward.setRewardItem(dto.rewardItem());
        reward.setItemSize(dto.itemSize());
        reward.setProduct(product);
        return reward;
    }

    public static List<RewardReadResponseDTO> mapToDTOList(List<Reward> rewardList) {
        return rewardList.stream()
                .map(RewardMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    public static RewardReadResponseDTO mapToDTO(Reward reward) {
        return new RewardReadResponseDTO(
                reward
        );
    }
}