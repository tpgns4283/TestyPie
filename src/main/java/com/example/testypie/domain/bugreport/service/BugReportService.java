package com.example.testypie.domain.bugreport.service;

import com.example.testypie.domain.bugreport.dto.BugReportRequestDTO;
import com.example.testypie.domain.bugreport.dto.BugReportResponseDTO;
import com.example.testypie.domain.bugreport.entity.BugReport;
import com.example.testypie.domain.bugreport.repository.BugReportRepository;
import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.product.service.ProductService;
import com.example.testypie.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BugReportService {

    private final BugReportRepository bugReportRepository;
    private final ProductService productService;

    @Transactional
    public BugReportResponseDTO createBugReport(Long productId, BugReportRequestDTO req,
        User user) {
        Product product = productService.findProduct(productId);
        BugReport bugReport =
            BugReport.builder().content(req.content()).product(product).user(user).build();

        BugReport saveBugReport = bugReportRepository.save(bugReport);

        return BugReportResponseDTO.of(saveBugReport);

    }
}
