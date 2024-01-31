package com.example.testypie.domain.feedback.service;

import com.example.testypie.domain.category.entity.Category;
import com.example.testypie.domain.category.service.CategoryService;
import com.example.testypie.domain.feedback.dto.FeedbackCreateRequestDTO;
import com.example.testypie.domain.feedback.dto.FeedbackCreateResponseDTO;
import com.example.testypie.domain.feedback.dto.FeedbackDetailsCreateRequestDTO;
import com.example.testypie.domain.feedback.entity.Feedback;
import com.example.testypie.domain.feedback.entity.FeedbackDetails;
import com.example.testypie.domain.feedback.repository.FeedbackRepository;
import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.product.service.ProductService;
import com.example.testypie.domain.survey.entity.Survey;
import com.example.testypie.domain.survey.service.SurveyService;
import com.example.testypie.domain.user.dto.RatingStarRequestDTO;
import com.example.testypie.domain.user.entity.User;
import com.example.testypie.global.exception.ErrorCode;
import com.example.testypie.global.exception.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final SurveyService surveyService;


    public FeedbackCreateResponseDTO addFeedback(FeedbackCreateRequestDTO req, Long productId, User user,
                                                 Long childCategoryId, String parentCategoryName) {
        // 해당 product 존재 여부 검증
        Product product = productService.findProduct(productId);
        if(product.getUser().getId().equals(user.getId())){
            throw new IllegalStateException("Product작성자는 피드백을 제출할 수 없습니다.");
        }
        Category category = categoryService.getCategory(childCategoryId, parentCategoryName);
        Survey survey = surveyService.getSurveyById(product.getSurvey().getId());

        boolean hasSubmitted = feedbackRepository.existsByUserAndSurvey(user, survey);
        if (hasSubmitted) {
            throw new IllegalStateException("이미 피드백을 제출하셨습니다.");
        }
        
        if(LocalDateTime.now().isAfter(product.getClosedAt())){
            throw new IllegalStateException("해당 Product의 기간이 만료되었습니다.");
        }

        if (!category.getId().equals(product.getCategory().getId())) {
            throw new GlobalExceptionHandler.CustomException(ErrorCode.SELECT_PRODUCT_CATEGORY_NOT_FOUND);
        }

        Feedback feedback = Feedback.builder()
                .createdAt(LocalDateTime.now())
                .user(user)
                .product(product)
                .survey(survey)
                .build();

        List<FeedbackDetails> detailslist = new ArrayList<>();
        for (FeedbackDetailsCreateRequestDTO feedbackDetailsDTO : req.feedbackDetailsList()) {
            FeedbackDetails feedbackDetails = FeedbackDetails.builder()
                    .feedback(feedback)
                    .response(feedbackDetailsDTO.response())
                    .build();

            detailslist.add(feedbackDetails);
        }

        feedback.setFeedbackDetailsList(detailslist);
        Feedback savedFeedback = feedbackRepository.save(feedback);
        return new FeedbackCreateResponseDTO(savedFeedback);
    }


    //=======================================================================//
    //    에러클래스 수정함
    //=======================================================================//

    //feedback_id로 feedback 조회
    @Transactional(readOnly = true)
    public Feedback getFeedbackById(Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.CustomException(ErrorCode.SELECT_FEEDBACK_NOT_FOUND));
    }

    //product의 feedback인지 확인
    public void checkFeedback(Feedback feedback, Long product_id) {
        if (!feedback.getProduct().getId().equals(product_id)) {
            throw new GlobalExceptionHandler.CustomException(ErrorCode.SELECT_FEEDBACK_NOT_FOUND);
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
                .orElseThrow(() -> new GlobalExceptionHandler.CustomException(ErrorCode.SELECT_FEEDBACK_NOT_FOUND));
    }

    public void setFeedbackRatingStar(Feedback feedback, RatingStarRequestDTO req) {
        feedback.assignRating(req);
        feedbackRepository.save(feedback);
    }

    // 5점을 받은 피드백의 리스트를 가져오는 메서드
    public List<Feedback> findFiveStarFeedbacksByProduct(Long productId) {
        return feedbackRepository.findFeedbacksByProductIdAndRating(productId, 5.0);
    }

}
