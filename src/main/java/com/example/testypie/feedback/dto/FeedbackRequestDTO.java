package com.example.testypie.feedback.dto;

import com.example.testypie.user.entity.User;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public record FeedbackRequestDTO (

    double grade,

   String title,

    LocalDateTime modifiedAt,

    String content,

    User user
) {

}
