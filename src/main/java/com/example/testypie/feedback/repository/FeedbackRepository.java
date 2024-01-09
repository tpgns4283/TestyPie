package com.example.testypie.feedback.repository;

import com.example.testypie.feedback.entity.Feedback;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    Optional<Feedback> findByTitle(String Title);

    List<Feedback> findAllByOrderByCreatedAtDesc();

    Optional<Feedback> findByGrade(double grade);
}
