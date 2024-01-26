package com.example.testypie.domain.survey.controller;

import com.example.testypie.domain.survey.dto.SurveyCreateRequestDTO;
import com.example.testypie.domain.survey.dto.SurveyCreateResponseDTO;
import com.example.testypie.domain.survey.dto.SurveyReadResponseDTO;
import com.example.testypie.domain.survey.service.SurveyService;
import com.example.testypie.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SurveyController {
    private final SurveyService surveyService;

    @GetMapping("/category/{parentCategoryName}/{childCategoryId}/products/{productId}/survey/{surveyId}")
    public ResponseEntity<SurveyReadResponseDTO> getSurvey(
            @PathVariable Long surveyId,
            @PathVariable Long productId,
            @PathVariable Long childCategoryId,
            @PathVariable String parentCategoryName) {
        SurveyReadResponseDTO res = surveyService.getSurvey(surveyId, productId, childCategoryId, parentCategoryName);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/category/{parentCategory_name}/{childCategory_id}/products/{product_id}/survey")
    public ResponseEntity<SurveyCreateResponseDTO> addSurvey(@RequestBody SurveyCreateRequestDTO req,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                             @PathVariable Long product_id,
                                                             @PathVariable Long childCategory_id,
                                                             @PathVariable String parentCategory_name) {
        SurveyCreateResponseDTO res = surveyService.addSurvey(req, product_id, userDetails.getUser(), childCategory_id, parentCategory_name);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
