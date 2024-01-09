package com.example.testypie.feedback.service;

import com.example.testypie.feedback.dto.FeedbackRequestDTO;
import com.example.testypie.feedback.dto.FeedbackResponseDTO;
import com.example.testypie.feedback.entity.Feedback;
import com.example.testypie.feedback.repository.FeedbackRepository;
import com.example.testypie.product.entity.Product;
import com.example.testypie.product.service.ProductService;
import com.example.testypie.user.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final ProductService productService;

    public FeedbackResponseDTO addFeedback(FeedbackRequestDTO req, Long product_id, User user) {
        // 해당 product 존재 여부 검증
        Product product = productService.findProduct(product_id);

        Feedback feedback = new Feedback(req, product, user);
        Feedback saveFeedback = feedbackRepository.save(feedback);
        return new FeedbackResponseDTO(saveFeedback);
    }

    @Transactional(readOnly = true)
    public FeedbackResponseDTO getFeedback(Long feedback_id, Long product_id) {
        Feedback feedback = getFeedbackById(feedback_id);
        checkFeedback(feedback, product_id);

        return new FeedbackResponseDTO(feedback);
    }

    @Transactional(readOnly = true)
    public List<FeedbackResponseDTO> getFeedbacks(Long product_id) {
        productService.findProduct(product_id);
        return feedbackRepository.findAllByOrderByCreatedAtDesc().stream()
            .map(FeedbackResponseDTO::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public FeedbackResponseDTO updateFeedback(Long product_id, FeedbackRequestDTO req, User user, Long feedback_id) {
        Feedback feedback = getFeedbackById(feedback_id);
        checkFeedback(feedback, product_id);
        checkUser(feedback, user.getId());

        Product product = productService.findProduct(product_id);
        feedback.update(product, req);
        return new FeedbackResponseDTO(feedback);
    }

    @Transactional
    public void deleteFeedback(Long product_id, User user, Long feedback_id) {
        Feedback feedback = getFeedbackById(feedback_id);
        checkFeedback(feedback, product_id);
        checkUser(feedback, user.getId());
        feedbackRepository.delete(feedback);
    }

    //=======================================================================//
    //    에러클래스 수정함
    //=======================================================================//

    //feedback_id로 feedback 조회
    @Transactional(readOnly = true)
    public Feedback getFeedbackById(Long id) {
        return feedbackRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("feedbackId"));
    }

    //product의 feedback인지 확인
    public void checkFeedback(Feedback feedback, Long product_id) {
        if (!feedback.getProduct().getId().equals(product_id)) {
            throw new IllegalArgumentException("feedback's productId");
        }
    }

    //feedback 작성 유저인지 확인
    public void checkUser(Feedback feedback, Long user_id) {
        if(!feedback.getUser().getId().equals(user_id)) {
            throw new IllegalArgumentException("feedback's modifier");
        }
    }

}
