package com.example.testypie.domain.category.entity;

import jakarta.persistence.*;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category parent;

    @Column private Long depth;

    @Builder
    private Category(Long id, String name, Long depth, Category parent) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        if (Objects.isNull(parent)) {
            this.depth = depth;
        } else {
            this.depth = depth + 1;
        }
    }
}
