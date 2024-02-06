package com.example.testypie.domain.bugreport.dto.response;

import com.example.testypie.domain.bugreport.entity.BugReport;
import java.time.LocalDateTime;
import org.springframework.lang.Nullable;

public record CreateBugReportResponseDTO(
    Long id,
    Long productId,
    LocalDateTime createdAt,
    String content,
    @Nullable String fileUrl,
    Long userId) {

  public static CreateBugReportResponseDTO of(BugReport bugReport) {
    return new CreateBugReportResponseDTO(
        bugReport.getId(),
        bugReport.getProduct().getId(),
        bugReport.getCreatedAt(),
        bugReport.getContent(),
        bugReport.getFileUrl(),
        bugReport.getUser().getId());
  }
}
