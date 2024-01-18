package com.example.testypie.domain.reward.entity;
import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.reward.dto.RewardCreateRequestDTO;
import com.example.testypie.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Reward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String rewardItem;

    @Column
    private Long itemSize;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Product product;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    private Reward(Long id, String reward_item, Long item_size, User user, Product product) {
        this.id = id;
        this.rewardItem = reward_item;
        this.itemSize = item_size;
        this.user = user;
        this.product = product;
    }

    public void setProduct(Product saveProduct) {
        this.product = saveProduct;
    }
}
