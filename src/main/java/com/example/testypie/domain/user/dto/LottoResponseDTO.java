package com.example.testypie.domain.user.dto;

import com.example.testypie.domain.user.entity.User;

import java.util.List;

public record LottoResponseDTO(
        List<User> userList
) {
}
