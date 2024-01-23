package com.example.testypie.domain.bugreport.controller;

import com.example.testypie.domain.bugreport.dto.BugReportRequestDTO;
import com.example.testypie.domain.bugreport.dto.BugReportResponseDTO;
import com.example.testypie.domain.bugreport.service.BugReportService;
import com.example.testypie.domain.user.entity.User;
import com.example.testypie.domain.user.service.UserInfoService;
import com.example.testypie.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category/{parentCategory_name}/{childCategory_id}/products/{product_id}/report")
@RequiredArgsConstructor
public class BugReportController {

    private BugReportService bugReportService;
    private UserInfoService userInfoService;

    @PostMapping
    public ResponseEntity<BugReportResponseDTO> createBugReport(
        @PathVariable Long product_id,
        @RequestBody @Valid BugReportRequestDTO req,
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long childCategory_id,
        @PathVariable String parentCategory_name) {
        User user = userDetails.getUser();

        BugReportResponseDTO res = bugReportService.createBugReport(product_id, req, user);

        return ResponseEntity.ok().body(res);
    }
}
/*
<버그리포트>
프로덕트에 버그 신고하기 버튼으로 생성
신고에 유저 검증은 필요 없음

 */