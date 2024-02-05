package com.example.testypie.domain.product.dto;

import lombok.NonNull;

public record ProductCommonCreateRequestDTO(@NonNull String title, @NonNull String content) {}
