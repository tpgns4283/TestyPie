package com.example.testypie.domain.user.dto;

import jakarta.validation.constraints.Pattern;
import org.springframework.web.multipart.MultipartFile;

public record ProfileRequestDTO(

    @Pattern(regexp = "^[a-zA-Z0-9]{8,16}$", message = "비밀번호는 소문자, 대문자, 숫자로 이루어진 8~16자리입니다.")
    String password,
    @Pattern(regexp = "^[a-zA-Z0-9가-힣_]{3,20}$", message = "닉네임 형식이 올바르지 않습니다.")
    String nickname,
    String description
) {}