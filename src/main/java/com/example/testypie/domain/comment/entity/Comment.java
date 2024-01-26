package com.example.testypie.domain.comment.entity;

import com.example.testypie.domain.comment.dto.CommentRequestDTO;
import com.example.testypie.domain.commentLike.entity.CommentLike;
import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.user.entity.User;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    private Long commentLikeCnt;

    @Column
    private LocalDateTime createAt;

    @Column
    private LocalDateTime modifiedAt;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "product_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @OneToMany(mappedBy = "comment", targetEntity = CommentLike.class, cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<CommentLike> commentLikeList = new ArrayList<>();

    @Builder
    private Comment(Long id, String content, Long commentLikeCnt, LocalDateTime createAt, LocalDateTime modifiedAt,
            User user, Product product) {
        this.id = id;
        this.content = content;
        this.commentLikeCnt = commentLikeCnt;
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;
        this.user = user;
        this.product = product;
    }

    public void update(CommentRequestDTO req, Product product) {
        this.content = req.content();
        this.product = product;
        this.modifiedAt = LocalDateTime.now();
    }

    public void updateCommentLikeCnt(boolean clickCommentLike) {
        if (clickCommentLike) {
            this.commentLikeCnt++;
            return;
        }
        this.commentLikeCnt--;
    }
}
