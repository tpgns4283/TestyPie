package com.example.testypie.user.dto;

import com.example.testypie.user.entity.User;

public record ProfileResponseDTO(
        String nickname,
        String description

        // 이미지 가져오기
        // *************
) {
    public static ProfileResponseDTO of(User user) { return new ProfileResponseDTO(user.getNickname(), user.getDescription()); }
}
