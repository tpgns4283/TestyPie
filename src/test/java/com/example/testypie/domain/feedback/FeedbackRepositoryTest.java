// package com.example.testypie.domain.feedback;
//
// import static org.junit.jupiter.api.Assertions.assertEquals;
//
// import com.example.testypie.domain.feedback.entity.Feedback;
// import com.example.testypie.domain.feedback.repository.FeedbackRepository;
// import com.example.testypie.domain.product.entity.Product;
// import com.example.testypie.domain.product.repository.ProductRepository;
// import com.example.testypie.domain.user.entity.User;
// import com.example.testypie.domain.user.repository.UserRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
// import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// import org.springframework.test.annotation.DirtiesContext;
// import org.springframework.test.context.ActiveProfiles;
//
// @DataJpaTest
// @ActiveProfiles("test")
// @AutoConfigureTestDatabase(replace = Replace.NONE)
// class FeedbackRepositoryTest {
//
//    @Autowired
//    private FeedbackRepository feedbackRepository;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private ProductRepository productRepository;
//
//    User user;
//    Product product;
//
//    @BeforeEach
//    void init() {
//        user = userRepository.saveAndFlush(User.builder()
//                .id(1L)
//                .account("test1234")
//                .password("12345678")
//                .email("test@gmail.com")
//                .nickname("tester")
//                .description("hello im test")
//                .build());
//
//        product = productRepository.saveAndFlush(Product.builder()
//                .id(1L)
//                .title("test title")
//                .content("test content")
//                .productLikeCnt(0L)
//                .user(user)
//                .build());
//    }
//
//    @DisplayName("feedback Rating 평균 구하는 쿼리 테스트")
//    @Test
//    @DirtiesContext
//    void testFindAverageRatingByUserId() throws Exception {
//        //given
//        // 테스트용 feedback 데이터 생성
//
//        feedbackRepository.save(Feedback.builder()
//                .id(1L)
//                .user(user)
//                .product(product)
//                .rating(3.0)
//                .build());
//        feedbackRepository.save(Feedback.builder()
//                .id(2L)
//                .user(user)
//                .product(product)
//                .rating(3.0)
//                .build());
//
//        feedbackRepository.save(Feedback.builder()
//                .id(3L)
//                .user(user)
//                .product(product)
//                .rating(3.0)
//                .build());
//        //when
//        Double average = feedbackRepository.findAverageScoreByUserId(user.getId());
//
//        //then
//        assertEquals(3.0, average, 0.1);
//    }
// }
