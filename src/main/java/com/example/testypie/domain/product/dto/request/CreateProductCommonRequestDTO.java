package com.example.testypie.domain.product.dto.request;

import lombok.NonNull;

public record CreateProductCommonRequestDTO(@NonNull String title, @NonNull String content) {}
