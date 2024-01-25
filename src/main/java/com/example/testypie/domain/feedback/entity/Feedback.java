package com.example.testypie.domain.feedback.entity;

import com.example.testypie.domain.answer.entity.Answer;
import com.example.testypie.domain.feedback.dto.FeedbackRequestDTO;
import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.question.entity.Question;
import com.example.testypie.domain.user.dto.RatingStarRequestDTO;
import com.example.testypie.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Entity
@NoArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private LocalDateTime createdAt;

    @JoinColumn(name = "feedback_id")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questionList;

    @JoinColumn(name = "feedback_id")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Answer> answerList;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column
    private Double rating;

    @Builder
    private Feedback(Long id, String title, LocalDateTime createdAt, List<Question> questionList,
                     Double rating, User user, Product product) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.questionList = questionList;
        this.rating = rating;
        this.user = user;
        this.product = product;
    }

    public void assignRating(RatingStarRequestDTO req) {
        this.rating = req.rating();
    }
}
