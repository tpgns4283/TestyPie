package com.example.testypie.feedback.dto;

import com.example.testypie.user.entity.User;
import java.time.LocalDateTime;
import lombok.Getter;

public record FeedbackRequestDTO (

        Double grade,
        String title,
        String content
) {

}
