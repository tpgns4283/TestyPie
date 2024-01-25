package com.example.testypie.domain.feedback.service;

import com.example.testypie.domain.feedback.entity.Feedback;
import com.example.testypie.domain.feedback.repository.FeedbackRepository;
import com.example.testypie.domain.user.dto.RatingStarRequestDTO;
import com.example.testypie.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;



//    private Product verifyUserNotCreateProduct(Long productId, User user) {
//        Product product = productService.findProduct(productId);
//
//        //RuntimeException으로 변경 예정
//        if (user.getId().equals(product.getUser().getId())) {
//            throw new RejectedExecutionException("본인은 피드백을 작성할 수 없습니다.");
//        }
//        return product;
//    }


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
