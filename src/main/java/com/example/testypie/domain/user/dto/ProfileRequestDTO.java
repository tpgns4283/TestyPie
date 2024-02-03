package com.example.testypie.domain.user.dto;

public record ProfileRequestDTO(

    String password,
    String nickname,
    String description) {}
