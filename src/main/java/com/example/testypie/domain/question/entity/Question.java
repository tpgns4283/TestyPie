package com.example.testypie.domain.question.entity;

import com.example.testypie.domain.option.entity.Option;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id") // Option 테이블에 question_id 외래키 추가
    private List<Option> options;

}
