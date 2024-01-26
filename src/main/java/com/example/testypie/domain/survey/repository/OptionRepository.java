package com.example.testypie.domain.survey.repository;

import com.example.testypie.domain.survey.entity.Option;
import com.example.testypie.domain.survey.entity.Question;
import com.example.testypie.domain.survey.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<Survey, Long> {
    List<Option> findByQuestion(Question question);
}

