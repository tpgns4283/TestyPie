package com.example.testypie.domain.user.dto;

import com.example.testypie.domain.user.entity.User;

public record ProfileResponseDTO(
        String nickname,
        String description

        // 이미지 가져오기
        // *************
) {
    public static ProfileResponseDTO of(User user) { return new ProfileResponseDTO(user.getNickname(), user.getDescription()); }
}
