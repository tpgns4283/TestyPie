package com.example.testypie.domain.feedback.service;

import com.example.testypie.domain.category.entity.Category;
import com.example.testypie.domain.category.service.CategoryService;
import com.example.testypie.domain.feedback.dto.FeedbackRequestDTO;
import com.example.testypie.domain.feedback.dto.FeedbackResponseDTO;
import com.example.testypie.domain.feedback.entity.Feedback;
import com.example.testypie.domain.feedback.repository.FeedbackRepository;
import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.product.service.ProductService;
import com.example.testypie.domain.question.dto.QuestionCreateRequestDTO;
import com.example.testypie.domain.question.dto.QuestionMapper;
import com.example.testypie.domain.user.dto.RatingStarRequestDTO;
import com.example.testypie.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final ProductService productService;
    private final CategoryService categoryService;

    @Transactional
    public FeedbackResponseDTO addFeedback(FeedbackRequestDTO req, Long product_id, User user, Long childCategory_id, String parentCategory_name) {

        Product product = verifyUserNotCreateProduct(product_id, user);
        Category category = categoryService.getCategory(childCategory_id, parentCategory_name);

        List<QuestionCreateRequestDTO> questionList = req.questionList();

        if(category.getId().equals(product.getCategory().getId())) {
            Feedback feedback = Feedback.builder()
                    .user(user)
                    .title(req.title())
                    .createdAt(LocalDateTime.now())
                    .product(product)
                    .build();

            feedback.setQuestionList(QuestionMapper.mapToEntityList(questionList, feedback));

            Feedback saveFeedback = feedbackRepository.save(feedback);
            return new FeedbackResponseDTO(saveFeedback);
        }else{
            throw new IllegalArgumentException("카테고리와 상품카테고리가 일치하지 않습니다.");
        }
    }

    private Product verifyUserNotCreateProduct(Long productId, User user) {
        Product product = productService.findProduct(productId);

        //RuntimeException으로 변경 예정
        if (user.getId().equals(product.getUser().getId())) {
            throw new RejectedExecutionException("본인은 피드백을 작성할 수 없습니다.");
        }
        return product;
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


    public double getAverageRating(User user) {
        return feedbackRepository.findAverageScoreByUserId(user.getId());
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
