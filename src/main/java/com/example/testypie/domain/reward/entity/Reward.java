package com.example.testypie.domain.reward.entity;
import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
public class Reward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String reward_item;

    @Column
    private Long item_size;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    private Reward(Long id, String reward_item, Long item_size, User user) {
        this.id = id;
        this.reward_item = reward_item;
        this.item_size = item_size;
        this.user = user;
    }

    public void setProduct(Product saveProduct) {
        this.product = saveProduct;
    }
}
