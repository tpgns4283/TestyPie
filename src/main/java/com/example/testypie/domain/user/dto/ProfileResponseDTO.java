package com.example.testypie.domain.user.dto;

import com.example.testypie.domain.user.entity.User;

public record ProfileResponseDTO(
        String nickname,
        String description,

        String fileUrl
) {
    public static ProfileResponseDTO of(User user) { return new ProfileResponseDTO(user.getNickname(), user.getDescription(), user.getFileUrl()); }
}
