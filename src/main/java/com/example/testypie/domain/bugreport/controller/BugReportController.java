package com.example.testypie.domain.bugreport.controller;

import com.example.testypie.domain.bugreport.dto.BugReportRequestDTO;
import com.example.testypie.domain.bugreport.dto.BugReportResponseDTO;
import com.example.testypie.domain.bugreport.service.BugReportService;
import com.example.testypie.global.security.UserDetailsImpl;
import jakarta.validation.constraints.NotNull;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/category/{parentCategory_name}/{childCategory_id}/products/{product_id}")
@RequiredArgsConstructor
public class BugReportController {

    private BugReportService bugReportService;

    @PostMapping(value = "/reports/{reportId}", consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BugReportResponseDTO> createBugReport(@PathVariable Long product_id,
            @RequestPart(value = "req", required = false) BugReportRequestDTO req,
            @RequestPart(value = "file", required = false) @Nullable MultipartFile multipartFile,
            @AuthenticationPrincipal @NotNull UserDetailsImpl userDetails) {

        try {
            BugReportResponseDTO res = bugReportService.createBugReport(product_id, req,
                    userDetails.getUser(), multipartFile);
            return ResponseEntity.ok().body(res);
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("createBugReport NoSuchElement");
        } catch (Exception e) {
            throw new IllegalArgumentException("createBugReport");
        }
    }

    @GetMapping("/reports/{bugReport_id}")  // 경로 중괄호가 잘못 닫혔습니다.
    public ModelAndView getProduct(@PathVariable Long bugReport_id, @PathVariable Long product_id,
            @AuthenticationPrincipal @NotNull UserDetailsImpl userDetails) {

        BugReportResponseDTO res = bugReportService.getBugReport(bugReport_id, product_id,
                userDetails.getUser());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("bugReport");
        modelAndView.addObject("bugReport", res);
        return modelAndView;
    }

    @GetMapping("/reports")
    public ResponseEntity<Page<BugReportResponseDTO>> getBugReports(@PathVariable Long product_id,
            @AuthenticationPrincipal @NotNull UserDetailsImpl userDetails, Pageable pageable) {

        Page<BugReportResponseDTO> res = bugReportService.getBugReports(pageable, product_id,
                userDetails.getUser());

        return ResponseEntity.ok().body(res);
    }
}

/*
<버그리포트>
프로덕트에 버그 신고하기 버튼으로 생성
신고에 유저 검증은 필요 없음
 */