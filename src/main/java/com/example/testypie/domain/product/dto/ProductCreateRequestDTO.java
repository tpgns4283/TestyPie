package com.example.testypie.domain.product.dto;

import java.util.Optional;

public record ProductCreateRequestDTO(
    ProductCommonCreateRequestDTO commonCreateRequestDTO,
    Optional<ProductTestCreateRequestDTO> testCreateRequestDTO) {}
