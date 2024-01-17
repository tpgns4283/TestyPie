package com.example.testypie.domain.product.dto;

import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.reward.entity.Reward;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public record ProductReadResponseDTO (
        Long id,
        String title,
        String content,
        String category,
        LocalDateTime createAt,
        LocalDateTime startAt,
        LocalDateTime closedAt,
        List<Reward> rewardList,
        String message
) {
    public static ProductReadResponseDTO of(Product product) throws ParseException {

        String end = product.getClosedAt().toString();
        String now = LocalDateTime.now().toString();

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date nowFormat = format.parse(now);
        Date endFormat = format.parse(end);

        long diffDays;
        String message;

        if(LocalDateTime.now().isBefore(product.getStartedAt())) {
            message = "Product 시작 전입니다.";
        } else {
            long diffSec = (endFormat.getTime() - nowFormat.getTime()) / 1000;
            diffDays = (diffSec / (24 * 60 * 60));
            message = "마감 " +diffDays+"일 전";
        }

        return new ProductReadResponseDTO(
                product.getId(),
                product.getTitle(),
                product.getContent(),
                product.getCategory().getName(),
                product.getCreatedAt(),
                product.getStartedAt(),
                product.getClosedAt(),
                product.getRewardList(),
                message
        );
    }
}