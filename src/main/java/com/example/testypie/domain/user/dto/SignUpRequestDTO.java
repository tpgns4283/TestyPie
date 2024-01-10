package com.example.testypie.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignUpRequestDTO(
        @NotBlank
        @Pattern(regexp = "^[a-z0-9]{6,12}$", message = "아이디는 소문자, 숫자로 이루어진 6~12자리입니다.")
        String account,

        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9]{8,16}$", message = "비밀번호는 소문자, 대문자, 숫자로 이루어진 8~16자리입니다.")
        String password,

        @NotBlank
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
        @Email
        String email,

        @Pattern(regexp = "^[a-zA-Z0-9가-힣_]{3,20}$", message = "닉네임 형식이 올바르지 않습니다.")
        String nickname,

        @NotBlank
        String description
) {}