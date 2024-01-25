package com.example.testypie.domain.feedback.entity;

import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.user.dto.RatingStarRequestDTO;
import com.example.testypie.domain.user.entity.User;
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
    private String response;

    @Column
    private LocalDateTime createdAt;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column
    private Double rating;

    @Builder
    private Feedback(Long id, String response, LocalDateTime createdAt, Double rating, User user, Product product) {
        this.id = id;
        this.response = response;
        this.createdAt = createdAt;
        this.user = user;
        this.product = product;
        this.rating = rating;
    }

    public void assignRating(RatingStarRequestDTO req) {
        this.rating = req.rating();
    }
}
