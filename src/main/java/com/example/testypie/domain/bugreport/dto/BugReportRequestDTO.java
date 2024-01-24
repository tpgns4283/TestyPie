package com.example.testypie.domain.bugreport.dto;

public record BugReportRequestDTO (
    Long reportProductId,
    String content,
    Long userId
) {
}
