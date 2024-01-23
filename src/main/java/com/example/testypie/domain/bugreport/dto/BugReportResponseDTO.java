package com.example.testypie.domain.bugreport.dto;

import com.example.testypie.domain.bugreport.entity.BugReport;
import com.example.testypie.domain.feedback.entity.Feedback;
import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

public record BugReportResponseDTO(
    Long id,
    Long reportedProductId,
//    LocalDateTime createdAt,
//    LocalDateTime modifiedAt,
    String content,
     //   String bugImgUrl,
    Long userId,
    Long productId
) {
    public static BugReportResponseDTO of (BugReport bugReport) {
        return new BugReportResponseDTO(
        bugReport.getId(),
        bugReport.getProduct().getId(),
        // bugReport.getCreatedAt(),
        // bugReport.getModifiedAt(),
        bugReport.getContent(),
        // bugReport.getBugImgUrl(),
        bugReport.getUser().getId(),
        bugReport.getProduct().getId()
        );
    }
}
