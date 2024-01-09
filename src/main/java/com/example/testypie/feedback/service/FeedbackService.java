package com.example.testypie.feedback.service;

import com.example.testypie.feedback.dto.FeedbackRequestDTO;
import com.example.testypie.feedback.dto.FeedbackResponseDTO;
import com.example.testypie.feedback.entity.Feedback;
import com.example.testypie.feedback.repository.FeedbackRepository;
import com.example.testypie.product.entity.Product;
import com.example.testypie.user.entity.User;
import com.example.testypie.user.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserService userService;
    private final ProductService productService;

    public FeedbackResponseDTO addFeedback(FeedbackRequestDTO req, User user)
        throws NotFoundException {
        // 해당 product 존재 여부 검증
        validateProductsExistence(req.getProducts());

        Product product = new Product(req, user);
        Product saveProduct = ProductRepository.save(product);
        return new ProductResponseDTO;
    }

    void validateProductsExistence(Product products) throws NotFoundException {
        for (Product product : products) {
            productService.validateProductExistence(product);
        }
    }

    @Transactional(readOnly = true)
    public FeedbackResponseDTO getFeedback(Long id) {
        Feedback feedback = getFeedbackById(id);
        return new FeedbackResponseDTO(feedback);
    }

    @Transactional(readOnly = true)
    public List<FeedbackResponseDTO> getFeedbacks() {
        return feedbackRepository.findAllByOrderByCreatedAtDesc().stream()
            .map(FeedbackResponseDTO::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public FeedbackResponseDTO updateFeedback(Long id, FeedbackRequestDTO req, User user)
        throws InvalidFeedbackModifierException {
        Feedback feedback = getFeedbackById(id);
        validateUserIsTester(feedback.getUser().getId(), user.getId());
        feedback.update(req);
        return new FeedbackResponseDTO(feedback);
    }

    public void deleteFeedback(Long id, User user) {
        Feedback feedback = getFeedbackById(id);
        feedbackRepository.delete(feedback);
    }

    @Transactional(readOnly = true)
    public Feedback getFeedbackById(Long id) {
        return feedbackRepository.findById(id)
            .orElseThrow(() -> new NotFoundFeedbackException("feedbackId", id.toString(),
                "주어진 id에 해당하는 게시글이 존재하지 않음"));
    }

    void validateUserIsTester(Long testerId, Long loggedInUserId)
        throws InvalidFeedbackModifierException {
        if (!testerId.equals(loggedInUserId)) {
            throw new InvalidFeedBackModfierException("tester", testerId.toString(),
                "이 게시물을 업데이트/삭제할 권한이 없습니다.");
        }
    }

}
