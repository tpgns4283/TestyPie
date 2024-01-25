package com.example.testypie.domain.user.service;

import com.example.testypie.domain.feedback.entity.Feedback;
import com.example.testypie.domain.feedback.service.FeedbackService;
import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.product.service.ProductService;
import com.example.testypie.domain.reward.entity.Reward;
import com.example.testypie.domain.user.dto.*;
import com.example.testypie.domain.user.entity.User;
import com.example.testypie.domain.user.repository.UserRepository;
import com.example.testypie.domain.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserInfoService {

    private final UserRepository userRepository;
    private final FeedbackService feedbackService;
    private final ProductService productService;
    private final S3Uploader s3Uploader;
    private final Random random = new Random();

    @Value("${default.image.address}")
    private String defaultProfileImageUrl;

    @Transactional
    public ProfileResponseDTO updateProfile(String account, ProfileRequestDTO req,
                                            MultipartFile multipartfile) {
        try {
            User profileUser = userRepository.findByAccount(account)
                    .orElseThrow(() -> new NoSuchElementException("해당하는 유저가 없습니다."));
            String fileUrl = req.fileUrl();

            if (multipartfile != null && !multipartfile.isEmpty()) {
                s3Uploader.upload(multipartfile, "profile/");
                fileUrl = s3Uploader.upload(multipartfile, "profile/");
            } else if (!fileUrl.equals(defaultProfileImageUrl)) {
                s3Uploader.upload(multipartfile, "profile/");
            } else {
                fileUrl = defaultProfileImageUrl;
            }

            profileUser.update(req);
            return new ProfileResponseDTO(profileUser.getAccount(), profileUser.getNickname(), profileUser.getEmail(), profileUser.getDescription(),
                    profileUser.getFileUrl());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("프로필 업데이트에 실패했습니다.", e);
        }
    }

    public ProfileResponseDTO getProfile(String account) {
        User user = findProfile(account);
        return new ProfileResponseDTO(user.getAccount(),
                user.getNickname(),
                user.getEmail(),
                user.getDescription(),
                user.getFileUrl());
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
        // product 찾기
        Product product = productService.findProduct(productId);

        // rewardlist 찾기
        List<Reward> rewardList = product != null ? product.getRewardList() : null;

        // 빈 목록이나 null일 경우 예외 처리
        if (rewardList == null || rewardList.isEmpty()) {
            return List.of();  // 빈 리스트 반환
        }

        // 선택된 랜덤 인덱스에 해당하는 User 가져오기 (중복 방지)
        return getRandomUserList(productId, rewardList);
    }

    private List<User> getRandomUserList(Long productId, List<Reward> rewardList) {
        // 전체 reward_size 합산
        int totalRewardSize = rewardList.stream()
                .mapToInt(reward -> Math.toIntExact(reward.getItemSize()))
                .sum();

        // user 리스트 가져오기
        List<User> userList = userRepository.findAllFeedbackUsersByProductId(productId);

        // 전체 reward_size 만큼의 랜덤 인덱스 선택
        List<Integer> randomIndexes = getRandomIndexes(totalRewardSize, userList.size());

        // 선택된 랜덤 인덱스에 해당하는 User 가져오기 (중복 방지)
        return randomIndexes.stream()
                .distinct()
                .map(userList::get)
                .collect(Collectors.toList());
    }

    private List<Integer> getRandomIndexes(int totalSize, int maxSize) {
        return random.ints(totalSize, 0, maxSize)
                .boxed()
                .toList();
    }
}
