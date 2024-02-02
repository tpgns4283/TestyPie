package com.example.testypie.domain.user.dto;

import jakarta.validation.constraints.Pattern;

public record ProfileRequestDTO(
        @Pattern(regexp = "^[a-zA-Z0-9]{8,16}$", message = "8~16자 사이의 소문자, 대문자, 숫자") String password,
        @Pattern(regexp = "^[a-zA-Z0-9가-힣_]{1,20}$", message = "1~20자 사이의 소문자, 대문자, 숫자")
                String nickname,
        @Pattern(regexp = "^[a-zA-Z0-9가-힣_]{0,20}$", message = "20자 미만의 소문자, 대문자, 숫자")
                String description) {}
