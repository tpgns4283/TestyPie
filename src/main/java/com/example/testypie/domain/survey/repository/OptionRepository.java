package com.example.testypie.domain.survey.repository;

import com.example.testypie.domain.survey.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Survey, Long> {
}

