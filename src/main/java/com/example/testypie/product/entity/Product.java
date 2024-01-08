package com.example.testypie.product.entity;

import com.example.testypie.category.entity.Category;
import com.example.testypie.comment.entity.Comment;
import com.example.testypie.feedback.entity.Feedback;
import com.example.testypie.reward.entity.Reward;
import com.example.testypie.reward.entity.RewardImage;
import com.example.testypie.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.awt.Image;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
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

    @Column
    private String content;

    @Enumerated(EnumType.STRING)
    private Category category;


    @OneToMany(mappedBy = "product", targetEntity = Reward.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Reward> rewardList = new ArrayList<>();

    @Column
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime closedAt;

    @OneToMany(mappedBy = "product", targetEntity = Comment.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "product", targetEntity = Feedback.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Feedback> feedbackList = new ArrayList<>();

    @Builder
    private Product(Long id, User user, Category category, String title, String content,
                     List<Reward> rewardList, LocalDateTime startedAt, LocalDateTime closedAt,
                    List<Comment> commentList, List<Feedback> feedbackList) {

        this.id = id;
        this.user = user;
        this.category = category;
        this.title = title;
        this.content = content;
        this.rewardList = rewardList;
        this.startedAt = startedAt;
        this.closedAt = closedAt;
        this.commentList = commentList;
        this.feedbackList = feedbackList;
    }
}
