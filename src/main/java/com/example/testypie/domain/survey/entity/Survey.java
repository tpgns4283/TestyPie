package com.example.testypie.domain.survey.entity;

import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.question.entity.Question;
import com.example.testypie.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private LocalDateTime createdAt;

    @JoinColumn(name = "survey_id")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questionList = new ArrayList<>();

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    private Survey(Long id, String title, LocalDateTime createdAt, List<Question> questionList,
                     User user, Product product) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.questionList = questionList;
        this.user = user;
        this.product = product;
    }

    public void setQuestionList(List<Question> questionList) {
        if (questionList != null) {
            this.questionList = questionList;
            questionList.forEach(question -> question.setSurvey(this));
        } else{
            throw new IllegalArgumentException("Feedback에 Question은 반드시 들어가야 합니다.");
        }
    }
}
