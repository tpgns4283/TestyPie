package com.example.testypie.domain.question.entity;

import com.example.testypie.domain.feedback.entity.Feedback;
import com.example.testypie.domain.option.entity.Option;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id") // Option 테이블에 question_id 외래키 추가
    private List<Option> optionList;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Feedback feedback;

    @Builder
    public Question(Long id, String text, QuestionType questionType, List<Option> optionList, Feedback feedback) {
        this.id = id;
        this.text = text;
        this.questionType = questionType;
        this.optionList = optionList;
        this.feedback = feedback;
    }
}
