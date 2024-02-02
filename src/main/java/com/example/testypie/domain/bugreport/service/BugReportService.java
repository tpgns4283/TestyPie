package com.example.testypie.domain.bugreport.service;

import com.example.testypie.domain.bugreport.dto.BugReportRequestDTO;
import com.example.testypie.domain.bugreport.dto.BugReportResponseDTO;
import com.example.testypie.domain.bugreport.entity.BugReport;
import com.example.testypie.domain.bugreport.repository.BugReportRepository;
import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.product.service.ProductService;
import com.example.testypie.domain.user.entity.User;
import com.example.testypie.domain.user.service.UserInfoService;
import com.example.testypie.domain.util.S3Util;
import com.example.testypie.domain.util.S3Util.FilePath;
import com.example.testypie.global.exception.ErrorCode;
import com.example.testypie.global.exception.GlobalExceptionHandler;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class BugReportService {

    private final BugReportRepository bugReportRepository;
    private final ProductService productService;
    private final UserInfoService userInfoService;
    private final S3Util s3Util;

    public BugReportResponseDTO createBugReport(Long productId, BugReportRequestDTO req,
            User user, MultipartFile multipartFile) {
        Product product = productService.findProduct(productId);

        String fileUrl = s3Util.uploadFile(multipartFile, FilePath.BUGREPORT);

        BugReport bugReport = BugReport.builder()
                .content(req.content())
                .product(product)
                .user(user)
                .fileUrl(fileUrl)
                .createdAt(LocalDateTime.now())
                .build();

        BugReport saveBugReport = bugReportRepository.save(bugReport);

        return BugReportResponseDTO.of(saveBugReport);
    }

    public BugReportResponseDTO getBugReport(Long bugReportId, Long productId, User user) {
        Product product = productService.findProduct(productId);

        // 해당 제품의 소유자가 아닌 경우
        if (!product.getUser().getId().equals(user.getId())) {
            throw new GlobalExceptionHandler.CustomException(
                    ErrorCode.SELECT_BUGREPORT_NOT_FOUND); //-> 권한이 없습니ㅏㄷ.
        }

        // 해당 제품에 대한 BugReport 조회 및 응답 DTO 생성
        return BugReportResponseDTO.of(
                bugReportRepository.findByProductIdAndAndId(productId, bugReportId)
                        .orElseThrow(() -> new GlobalExceptionHandler.CustomException(
                                ErrorCode.SELECT_BUGREPORT_NOT_FOUND)));
    }

    public Page<BugReportResponseDTO> getBugReports(Pageable pageable, Long productId, User user) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 10;
        Product product = productService.findProduct(productId);

        if (product.getUser().getId().equals(user.getId())) {
            Page<BugReport> bugReportPage = bugReportRepository.findAllByProductId(productId,
                    PageRequest.of(page, pageLimit, Sort.by(Direction.DESC, "id")));

            List<BugReportResponseDTO> resList = new ArrayList<>();

            for (BugReport bugReport : bugReportPage) {
                BugReportResponseDTO res = BugReportResponseDTO.of(bugReport);
                resList.add(res);
            }
            return new PageImpl<>(resList, pageable, bugReportPage.getTotalElements());
        }
        throw new IllegalArgumentException("클라이언트가 아닙니다.");
    }
}
