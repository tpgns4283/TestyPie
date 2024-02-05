package com.example.testypie.domain.product.dto;

public record ProductUpdateRequestDTO(
    String title, String content, String startAt, String closedAt) {}
