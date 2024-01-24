package com.example.testypie.domain.product.entity;

import com.example.testypie.domain.category.entity.Category;
import com.example.testypie.domain.comment.entity.Comment;
import com.example.testypie.domain.feedback.entity.Feedback;
import com.example.testypie.domain.productLike.entity.ProductLike;
import com.example.testypie.domain.reward.entity.Reward;
import com.example.testypie.domain.user.entity.User;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Category category;

    @Column(nullable = false)
    private Long productLikeCnt;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime modifiedAt;

    @Column
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime closedAt;

    @OneToMany(mappedBy = "product", targetEntity = Comment.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "product", targetEntity = Reward.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reward> rewardList = new ArrayList<>();

    @OneToMany(mappedBy = "product", targetEntity = Feedback.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Feedback> feedbackList = new ArrayList<>();

    @OneToMany(mappedBy = "product", targetEntity = ProductLike.class, cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<ProductLike> productLikeList = new ArrayList<>();

    @Builder
    private Product(Long id, User user, String title, String content, Category category,
            List<Reward> rewardList, LocalDateTime createAt, LocalDateTime modifiedAt,
            LocalDateTime startedAt, LocalDateTime closedAt,
            List<Comment> commentList, List<Feedback> feedbackList, Long productLikeCnt) {

        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        this.category = category;
        this.rewardList = rewardList;
        this.createdAt = createAt;
        this.modifiedAt = modifiedAt;
        this.startedAt = startedAt;
        this.closedAt = closedAt;
        this.commentList = commentList;
        this.feedbackList = feedbackList;
        this.productLikeCnt = productLikeCnt;
    }

    public void updateTitle(String title) {
        if (title != null) {
            this.title = title;
        }
    }

    public void updateContent(String content) {
        if (content != null) {
            this.content = content;
        }
    }

    public void updateModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public void updateStartAt(LocalDateTime startedAt) {
        if (startedAt != null) {
            this.startedAt = startedAt;
        }
    }

    public void updateClosedAt(LocalDateTime closedAt) {
        if (closedAt != null) {
            this.closedAt = closedAt;
        }
    }

    public void updateCategory(Category category) {
        if (category != null) {
            this.category = category;
        }
    }

    public void setRewardList(List<Reward> rewardList) {
        if (this.rewardList != null) {
            this.rewardList.forEach(reward -> reward.setProduct(null));
        }

        this.rewardList = rewardList;

        if (rewardList != null) {
            rewardList.forEach(reward -> reward.setProduct(this));
        }
    }

    public void updateProductLikeCnt(boolean clickProductLike) {
        if (clickProductLike) {
            this.productLikeCnt++;
            return;
        }
        this.productLikeCnt--;
    }
}
