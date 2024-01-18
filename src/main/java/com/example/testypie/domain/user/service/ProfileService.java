package com.example.testypie.domain.user.service;

import com.example.testypie.domain.feedback.service.FeedbackService;
import com.example.testypie.domain.feedback.entity.Feedback;
import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.user.dto.*;
import com.example.testypie.domain.user.entity.User;
import com.example.testypie.domain.user.repository.UserRepository;
import com.example.testypie.domain.user.dto.ProfileRequestDTO;
import com.example.testypie.domain.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final FeedbackService feedbackService;
    private final S3Uploader s3Uploader;

    @Transactional
    public ProfileResponseDTO updateProfile(String account, ProfileRequestDTO req) {
        User profileUser = userRepository.findByAccount(account)
                .orElseThrow(NoSuchElementException::new);
        profileUser.update(req);
        return new ProfileResponseDTO(profileUser.getNickname(), profileUser.getDescription(),
            profileUser.getFileUrl());
    }

    public ProfileResponseDTO getProfile(String account) {
        User user = findProfile(account);
        return new ProfileResponseDTO(user.getNickname(), user.getDescription(), user.getFileUrl());
    }

   public User findProfile(String account) {
        return userRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다."));
    }

    // 프로필 작성자와 사이트 이용자가 일치하는 메서드
    public void checkSameUser(String profileAccount, String userAccount) {
        User profileUser = findProfile(profileAccount);
        User user = findProfile(userAccount);
        if (!profileUser.equals(user)) {
            throw new IllegalArgumentException("요청하는 유저에게 권한이 없습니다.");
        }

    }

    // product 등록 이력 가져오기
    public List<RegisteredProductResponseDTO> getUserProducts(String account) {
        List<Product> productList = userRepository.getUserProductsOrderByCreatedAtDesc(account);
        return productList.stream()
                .map(RegisteredProductResponseDTO::of)
                .collect(Collectors.toList());
    }

    //product 참여 이력 가져오기
    public List<ParticipatedProductResponseDTO> getUserFeedbacks(String account) {
        return userRepository.getUserFeedbacksDtoIncludingProductInfo(account);
    }

    public Feedback getValidFeedback(Long productId, Long feedbackId) {
        // 제품 유효성 확인
        validateProduct(productId);

        // 피드백 검색 및 유효성 확인
        Feedback feedback = validateAndGetFeedback(productId, feedbackId);

        // feedback과 product의 연관성 확인
        validateFeedbackProductAssociation(feedback, productId);

        return feedback;
    }

    private void validateProduct(Long productId) {
        boolean isProductValid = userRepository.existsProductById(productId);
        if (!isProductValid) {
            throw new IllegalArgumentException("유효하지 않은 product ID: " + productId);
        }
    }

    private Feedback validateAndGetFeedback(Long productId, Long feedbackId) {
        Feedback feedback = feedbackService.getValidFeedback(productId, feedbackId);

        if (feedback == null) {
            throw new IllegalArgumentException("유효하지 않은 feedback ID: " + feedbackId);
        }

        return feedback;
    }

    private void validateFeedbackProductAssociation(Feedback feedback, Long productId) {
        if (!feedback.getProduct().getId().equals(productId)) {
            throw new RejectedExecutionException("피드백 ID " + feedback.getId() + "가 제품 " + productId + "과 연관되어 있지 않습니다.");
        }
    }

    public void assignRatingStarAtFeedback(Feedback feedback, RatingStarRequestDTO req) {
        feedbackService.setFeedbackRatingStar(feedback, req);
    }

    public double getAverageRating(String account) {
        User user = findProfile(account);
        return feedbackService.getAverageRating(user);
    }

    public List<User> drawUsers(Long productId) {
        // 1. 5점을 받은 사람들의 리스트 가져오고 한명뽑기. 뽑은 user 저장 상품갯수: -1하기

        // 2. 전체 리스트를 가져오고 뽑은 user 지우기, 0점 받은 사람 지우기
        
        // 3. 수정된 리스트에서 추첨 돌리기 상품 갯수가 0이될때까지
        
        // 4. 뽑힌 사람들을 리스트로 만들고 내보내기
        return null;
    }
}
