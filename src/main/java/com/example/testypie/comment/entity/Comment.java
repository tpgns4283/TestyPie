package com.example.testypie.comment.entity;

import com.example.testypie.product.entity.Product;
import com.example.testypie.user.entity.User;
import jakarta.persistence.*;
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

    @Column
    private String content;

    @Column
    private LocalDateTime createAt;

    @Column
    private LocalDateTime modifiedAt;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Builder
    private Comment(Long id, String content, LocalDateTime createAt, LocalDateTime modifiedAt, User user, Product product) {
        this.id = id;
        this.content = content;
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;
        this.user = user;
        this.product = product;
    }
}
