package com.example.testypie.domain.bugreport.dto;

import com.example.testypie.domain.bugreport.entity.BugReport;
import java.time.LocalDateTime;
import org.springframework.lang.Nullable;

public record BugReportResponseDTO(
    Long id,
    Long productId,
    LocalDateTime createdAt,
    String content,
    @Nullable
    String fileUrl,
    Long userId
) {

    public static BugReportResponseDTO of(BugReport bugReport) {
        return new BugReportResponseDTO(
            bugReport.getId(),
            bugReport.getProduct().getId(),
            bugReport.getCreatedAt(),
            bugReport.getContent(),
            bugReport.getFileUrl(),
            bugReport.getUser().getId()
        );
    }
}
