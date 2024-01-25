package com.example.testypie.domain.feedback.controller;

import com.example.testypie.domain.feedback.dto.FeedbackCreateRequestDTO;
import com.example.testypie.domain.feedback.dto.FeedbackReadResponseDTO;
import com.example.testypie.domain.feedback.dto.FeedbackResponseDTO;
import com.example.testypie.domain.feedback.service.FeedbackService;
import com.example.testypie.global.security.UserDetailsImpl;
import java.util.List;
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
public class FeedbackController {
    private final FeedbackService feedbackService;

    @GetMapping("/category/{parentCategory_name}/{childCategory_id}/products/{product_id}/feedback/{feedback_id}")
    public ResponseEntity<FeedbackReadResponseDTO> getFeedback(
            @PathVariable Long feedback_id,
            @PathVariable Long product_id,
            @PathVariable Long childCategory_id,
            @PathVariable String parentCategory_name) {
        FeedbackReadResponseDTO res = feedbackService.getFeedback(feedback_id, product_id, childCategory_id, parentCategory_name);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/category/{parentCategory_name}/{childCategory_id}/products/{product_id}/feedback")
    public ResponseEntity<List<FeedbackResponseDTO>> getFeedbacks(@PathVariable Long product_id,
                                                                  @PathVariable Long childCategory_id,
                                                                  @PathVariable String parentCategory_name) {
        List<FeedbackResponseDTO> resList = feedbackService.getFeedbacks(product_id, childCategory_id, parentCategory_name);
        return ResponseEntity.ok(resList);
    }

    @PostMapping("/category/{parentCategory_name}/{childCategory_id}/products/{product_id}/feedback")
    public ResponseEntity<FeedbackResponseDTO> addFeedback(@RequestBody FeedbackCreateRequestDTO req,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @PathVariable Long product_id,
                                                           @PathVariable Long childCategory_id,
                                                           @PathVariable String parentCategory_name) {
        FeedbackResponseDTO res = feedbackService.addFeedback(req, product_id, userDetails.getUser(), childCategory_id, parentCategory_name);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
