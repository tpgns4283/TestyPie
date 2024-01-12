package com.example.testypie.domain.feedback.entity;

import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.user.entity.User;
import com.example.testypie.domain.feedback.dto.FeedbackRequestDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Getter
@Entity
@NoArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Double grade;

    @Column
    private String title;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime modifiedAt;

    @Column
    private String content;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Builder
    private Feedback(Long id, Double grade, String title, LocalDateTime createdAt, LocalDateTime modifiedAt,
                     String content, User user, Product product) {
        this.id = id;
        this.grade = grade;
        this.title = title;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.content = content;
        this.user = user;
        this.product = product;
    }

    public Feedback(FeedbackRequestDTO req, Product product, User user) {
        this.title = req.title();
        this.content = req.content();
        this.grade = req.grade();
        this.createdAt = LocalDateTime.now();
        this.user = user;
        this.product = product;
    }

    public void update(Product product, FeedbackRequestDTO req) {
        this.grade = req.grade();
        this.modifiedAt = LocalDateTime.now();
        this.content = req.content();
        this.product = product;
    }
}
