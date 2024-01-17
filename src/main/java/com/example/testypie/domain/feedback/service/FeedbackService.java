package com.example.testypie.domain.feedback.service;

import com.example.testypie.domain.category.entity.Category;
import com.example.testypie.domain.category.service.CategoryService;
import com.example.testypie.domain.feedback.dto.FeedbackRequestDTO;
import com.example.testypie.domain.feedback.dto.FeedbackResponseDTO;
import com.example.testypie.domain.feedback.entity.Feedback;
import com.example.testypie.domain.feedback.repository.FeedbackRepository;
import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.product.service.ProductService;
import com.example.testypie.domain.user.dto.RatingStarRequestDTO;
import com.example.testypie.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final ProductService productService;
    private final CategoryService categoryService;

    public FeedbackResponseDTO addFeedback(FeedbackRequestDTO req, Long product_id, User user, Long childCategory_id, String parentCategory_name) {
        // 해당 product 존재 여부 검증
        Product product = productService.findProduct(product_id);
        Category category = categoryService.getCategory(childCategory_id, parentCategory_name);

        if(category.getId().equals(product.getCategory().getId())) {
            Feedback feedback = new Feedback(req, product, user);
            Feedback saveFeedback = feedbackRepository.save(feedback);
            return new FeedbackResponseDTO(saveFeedback);
        }else{
            throw new IllegalArgumentException("카테고리와 상품카테고리가 일치하지 않습니다.");
        }
    }

    @Transactional(readOnly = true)
    public FeedbackResponseDTO getFeedback(Long feedback_id, Long product_id, Long childCategory_id, String parentCategory_name) {

        Product product = productService.findProduct(product_id);
        Category category = categoryService.getCategory(childCategory_id, parentCategory_name);

        if(category.getId().equals(product.getCategory().getId())) {
            Feedback feedback = getFeedbackById(feedback_id);
            checkFeedback(feedback, product_id);
            return new FeedbackResponseDTO(feedback);
        }else{
            throw new IllegalArgumentException("카테고리와 상품카테고리가 일치하지 않습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<FeedbackResponseDTO> getFeedbacks(Long product_id, Long childCategory_id, String parentCategory_name) {
        Product product = productService.findProduct(product_id);
        Category category = categoryService.getCategory(childCategory_id, parentCategory_name);

        if(category.getId().equals(product.getCategory().getId())) {
            return feedbackRepository.findAllByOrderByCreatedAtDesc().stream()
                    .map(FeedbackResponseDTO::new)
                    .collect(Collectors.toList());
        }else{
            throw new IllegalArgumentException("카테고리와 상품카테고리가 일치하지 않습니다.");
        }
    }

    @Transactional
    public FeedbackResponseDTO updateFeedback(Long product_id, FeedbackRequestDTO req, User user, Long feedback_id, Long childCategory_id, String parentCategory_name) {

        Product product = productService.findProduct(product_id);
        Category category = categoryService.getCategory(childCategory_id, parentCategory_name);

        if(category.getId().equals(product.getCategory().getId())) {
            Feedback feedback = getFeedbackById(feedback_id);
            checkFeedback(feedback, product_id);
            checkUser(feedback, user.getId());
            feedback.update(product, req);
            return new FeedbackResponseDTO(feedback);
        }else{
            throw new IllegalArgumentException("카테고리와 상품카테고리가 일치하지 않습니다.");
        }
    }

    @Transactional
    public void deleteFeedback(Long product_id, User user, Long feedback_id, Long childCategory_id, String parentCategory_name) {

        Product product = productService.findProduct(product_id);
        Category category = categoryService.getCategory(childCategory_id, parentCategory_name);

        if(category.getId().equals(product.getCategory().getId())) {
            Feedback feedback = getFeedbackById(feedback_id);
            checkFeedback(feedback, product_id);
            checkUser(feedback, user.getId());
            feedbackRepository.delete(feedback);
        }else{
            throw new IllegalArgumentException("카테고리와 상품카테고리가 일치하지 않습니다.");
        }
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
        if (!feedback.getUser().getId().equals(user_id)) {
            throw new IllegalArgumentException("feedback's modifier");
        }
    }

    // productId와 feedbackId로 유효한 feedback 검색
    public Feedback getValidFeedback(Long productId, Long feedbackId) {
        //findByProductIdAndId를 사용하여 특정 prodcutId와 feedbackId에 해당하는 feedback검색
        return feedbackRepository.findByProductIdAndId(productId, feedbackId)
                //검색된 feedback 없으면 예외발생
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 feedback ID:" + feedbackId));
    }

    public void setFeedbackRatingStar(Feedback feedback, RatingStarRequestDTO req) {
        feedback.assignRating(req);
        feedbackRepository.save(feedback);
    }
}
