package com.example.testypie.domain.feedback.entity;

import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.survey.entity.Survey;
import com.example.testypie.domain.user.dto.RatingStarRequestDTO;
import com.example.testypie.domain.user.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Feedback {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column private LocalDateTime createdAt;

  @OneToMany(mappedBy = "feedback", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<FeedbackDetails> feedbackDetailsList = new ArrayList<>();

  @JoinColumn
  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "product_id")
  private Product product;

  @ManyToOne @JoinColumn private Survey survey;

  @Column private Double rating;

  @Builder
  private Feedback(
      Long id, LocalDateTime createdAt, Double rating, User user, Product product, Survey survey) {
    this.id = id;
    this.feedbackDetailsList = new ArrayList<>();
    this.createdAt = createdAt;
    this.user = user;
    this.product = product;
    this.rating = rating;
    this.survey = survey;
  }

  public void assignRating(RatingStarRequestDTO req) {
    this.rating = req.rating();
  }

  public void setFeedbackDetailsList(List<FeedbackDetails> detailslist) {
    if (detailslist != null) {
      this.feedbackDetailsList = detailslist;
      detailslist.forEach(feedbackDetails -> feedbackDetails.setFeedback(this));
    } else {
      throw new IllegalArgumentException("Feedback에 Question은 반드시 들어가야 합니다.");
    }
  }
}
