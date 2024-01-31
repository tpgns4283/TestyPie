package com.example.testypie.domain.product.dto;

import com.example.testypie.domain.comment.dto.CommentResponseDTO;
import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.reward.dto.RewardReadResponseDTO;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;

public record ProductReadResponseDTO(
        Long id,
        String nickname,
        String title,
        String content,
        String category,
        Long productLikeCnt,
        String createAt,
        String startAt,
        String closedAt,
        List<RewardReadResponseDTO> rewardList,
        String message

) {

    public static ProductReadResponseDTO of(Product product,
            List<RewardReadResponseDTO> rewardDTOList)
            throws ParseException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String createDate = product.getCreatedAt().format(formatter);
        String startDate = product.getClosedAt().format(formatter);
        String endDate = product.getClosedAt().format(formatter);

        String end = product.getClosedAt().toString();
        String now = LocalDateTime.now().toString();

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date nowFormat = format.parse(now);
        Date endFormat = format.parse(end);

        long diffDays;
        String message;

        if (LocalDateTime.now().isBefore(product.getStartedAt())) {
            message = "테스트 시작 전입니다.";
        }
        else if(LocalDateTime.now().isAfter(product.getClosedAt())){
            message = "마감된 테스트 입니다.";
        }
        else {
            long diffSec = (endFormat.getTime() - nowFormat.getTime()) / 1000;
            diffDays = (diffSec / (24 * 60 * 60));
            message = "마감 " + diffDays + "일 전";
        }

        return new ProductReadResponseDTO(
                product.getId(),
                product.getUser().getNickname(),
                product.getTitle(),
                product.getContent(),
                product.getCategory().getName(),
                product.getProductLikeCnt(),
                createDate,
                startDate,
                endDate,
                rewardDTOList,
                message
        );
    }
}