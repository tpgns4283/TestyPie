package com.example.testypie.product.entity;

import com.example.testypie.category.entity.Category;
import com.example.testypie.comment.entity.Comment;
import com.example.testypie.feedback.entity.Feedback;
import com.example.testypie.reward.entity.Reward;
import com.example.testypie.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

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
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column
    private String title;

    @Column
    private String content;

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(mappedBy = "product", targetEntity = Comment.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "product", targetEntity = Reward.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Reward> rewardList = new ArrayList<>();

    @Column
    private LocalDateTime createAt;

    @Column
    private LocalDateTime modifiedAt;

    @Column
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime closedAt;
    ;
    @OneToMany(mappedBy = "product", targetEntity = Feedback.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Feedback> feedbackList = new ArrayList<>();

    @Builder
    private Product(Long id, User user, Category category, String title, String content,
                     List<Reward> rewardList, LocalDateTime createAt, LocalDateTime modifiedAt, LocalDateTime startedAt, LocalDateTime closedAt,
                    List<Comment> commentList, List<Feedback> feedbackList) {

        this.id = id;
        this.user = user;
        this.category = category;
        this.title = title;
        this.content = content;
        this.rewardList = rewardList;
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;
        this.startedAt = startedAt;
        this.closedAt = closedAt;
        this.commentList = commentList;
        this.feedbackList = feedbackList;
    }

    public void updateTitle(String title) {
        if(title != null){
            this.title = title;
        }
    }
    public void updateContent(String content) {
        if(content != null){
            this.content = content;
        }
    }
    public void updateModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
    public void updateStartAt(LocalDateTime startedAt) {
        if(startedAt != null){
            this.startedAt = startedAt;
        }
    }
    public void updateClosedAt(LocalDateTime closedAt) {
        if(closedAt != null){
            this.closedAt = closedAt;
        }
    }
}
