package com.example.testypie.domain.reward.entity;
import com.example.testypie.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Reward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String reward_item;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Builder
    private Reward(Long id, String reward_item, Product product) {
        this.id = id;
        this.reward_item = reward_item;
        this.product = product;
    }
}
