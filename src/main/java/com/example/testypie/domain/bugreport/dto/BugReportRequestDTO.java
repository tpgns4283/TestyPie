package com.example.testypie.domain.bugreport.dto;

import org.springframework.web.multipart.MultipartFile;

public record BugReportRequestDTO (
    String content
) {
}
