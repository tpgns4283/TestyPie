package com.example.testypie.product.entity;

import com.example.testypie.category.entity.Category;
import com.example.testypie.feedback.entity.Feedback;
import com.example.testypie.reward.entity.Reward;
import com.example.testypie.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.awt.Image;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.events.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column
    private String title;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @Column
    private String content;

    @Column
    private Image productImg;

    @OneToMany(mappedBy = "Product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Reward> rewardList = new ArrayList<>();

    @Column
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime closedAt;

    @OneToMany(mappedBy = "Product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "Product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Feedback> feedbackList = new ArrayList<>();

    @Builder
    private Product(Long id, User user, Category category, String title, String content,
        Image productImg, List<Reward> rewardList, LocalDateTime startedAt, LocalDateTime closedAt,
        List<Comment> commentList, List<Feedback> feedbackList) {

        this.id = id;
        this.user = user;
        this.category = category;
        this.title = title;
        this.content = content;
        this.productImg = productImg;
        this.rewardList = rewardList;
        this.startedAt = startedAt;
        this.closedAt = closedAt;
        this.commentList = commentList;
        this.feedbackList = feedbackList;
    }
}
