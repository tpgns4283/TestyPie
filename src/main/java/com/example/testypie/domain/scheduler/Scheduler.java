package com.example.testypie.domain.scheduler;

import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.product.repositoy.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j(topic = "스케줄러")
@RequiredArgsConstructor
@Transactional
public class Scheduler {

    private final ProductRepository productRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * *")
    public void autoDelete() {
        LocalDateTime now = LocalDateTime.now();

        List<Product> productList = productRepository.findAll();

        for (Product p : productList) {
            if (now.isBefore(p.getClosedAt())) {
                productRepository.delete(p);
            }
        }
    }
}