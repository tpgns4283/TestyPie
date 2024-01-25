package com.example.testypie.domain.answer.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String response; // 사용자의 답변

    @Builder
    public Answer(Long id, String response){
        this.id = id;
        this.response = response;
    }
}
