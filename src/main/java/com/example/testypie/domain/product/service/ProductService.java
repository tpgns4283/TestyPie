package com.example.testypie.domain.product.service;

import static com.example.testypie.domain.product.constant.ProductConstant.DEFAULT_PRODUCT_LIKE_CNT;

import com.example.testypie.domain.category.entity.Category;
import com.example.testypie.domain.category.service.CategoryService;
import com.example.testypie.domain.comment.dto.CommentResponseDTO;
import com.example.testypie.domain.comment.service.CommentService;
import com.example.testypie.domain.product.dto.ProductCreateRequestDTO;
import com.example.testypie.domain.product.dto.ProductCreateResponseDTO;
import com.example.testypie.domain.product.dto.ProductDeleteResponseDTO;
import com.example.testypie.domain.product.dto.ProductPageResponseDTO;
import com.example.testypie.domain.product.dto.ProductReadResponseDTO;
import com.example.testypie.domain.product.dto.ProductUpdateRequestDTO;
import com.example.testypie.domain.product.dto.ProductUpdateResponseDTO;
import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.product.repository.ProductRepository;
import com.example.testypie.domain.reward.dto.RewardCreateRequestDTO;
import com.example.testypie.domain.reward.dto.RewardMapper;
import com.example.testypie.domain.reward.dto.RewardReadResponseDTO;
import com.example.testypie.domain.reward.entity.Reward;
import com.example.testypie.domain.user.entity.User;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final CommentService commentService;

    //CREATE
    @Transactional
    public ProductCreateResponseDTO createProduct(User user, ProductCreateRequestDTO req,
            String parentCategory_name, Long category_id) {

        Category category = categoryService.getCategory(category_id, parentCategory_name);

        List<RewardCreateRequestDTO> rewardList = req.rewardList();

        LocalDate startDate = LocalDate.parse(req.startAt());
        LocalDate closedDate = LocalDate.parse(req.closedAt());

        // 자정 시간과 함께 LocalDateTime 객체 생성
        LocalDateTime startAt = startDate.atStartOfDay();
        LocalDateTime closedAt = closedDate.atStartOfDay();

        Product product = Product.builder()
                .user(user)
                .title(req.title())
                .content(req.content())
                .category(category)
                .productLikeCnt(DEFAULT_PRODUCT_LIKE_CNT)
                .createAt(LocalDateTime.now())
                .startedAt(startAt)
                .closedAt(closedAt)
                .build();

        product.setRewardList(RewardMapper.mapToEntityList(rewardList, product));
        Product saveProduct = productRepository.save(product);
        return ProductCreateResponseDTO.of(saveProduct);
    }

    //READ
    public ProductReadResponseDTO getProduct(Long productId, Long category_id,
                                             String parentCategory_name)
            throws ParseException {

        Category category = categoryService.getCategory(category_id, parentCategory_name);
        Product product = findProduct(productId);

        List<Reward> rewardList = product.getRewardList();
        List<RewardReadResponseDTO> rewardDTOList = RewardMapper.mapToDTOList(rewardList);

        if (category.getId().equals(product.getCategory().getId())) {
            return ProductReadResponseDTO.of(product, rewardDTOList);
        } else {
            throw new IllegalArgumentException("카테고리와 상품카테고리가 일치하지 않습니다.");
        }
    }

    public Page<ProductPageResponseDTO> getProductPage(Pageable pageable,
            String parentCategory_name)
            throws ParseException {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 10;

        Page<Product> productPage = productRepository.findAll(
                PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        return getProductReadResponseDTOS(pageable, productPage);
    }

    public Page<ProductPageResponseDTO> getProductCategoryPage(Pageable pageable,
            Long childCategory_id,
            String parentCategory_name)
            throws ParseException {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 10;

        Category category = categoryService.getCategory(childCategory_id, parentCategory_name);

        Page<Product> productPage = productRepository.findAllByCategory_id(category.getId(),
                PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        return getProductReadResponseDTOS(pageable, productPage);
    }

    private Page<ProductPageResponseDTO> getProductReadResponseDTOS(Pageable pageable,
            Page<Product> productPage) {

        List<ProductPageResponseDTO> resList = new ArrayList<>();

        for (Product product : productPage) {
            ProductPageResponseDTO res = ProductPageResponseDTO.of(product);
            resList.add(res);
        }
        return new PageImpl<>(resList, pageable, productPage.getTotalElements());
    }

    //UPDATE
    public ProductUpdateResponseDTO updateProduct(Long productId, ProductUpdateRequestDTO req,
            User user, Long category_id,
            String parentCategory_name) {

        Product product = getUserProduct(productId, user);
        Category category = categoryService.getCategory(category_id, parentCategory_name);
        if (!category.getId().equals(product.getCategory().getId())) {
            throw new IllegalArgumentException("카테고리와 Product의 카테고리가 일치하지 않습니다.");
        }

            product.updateTitle(req.title());
            product.updateContent(req.content());
            product.updateCategory(category);
            product.updateModifiedAt(LocalDateTime.now());
            product.updateStartAt(req.startAt());
            product.updateClosedAt(req.closedAt());

            productRepository.save(product);

            return ProductUpdateResponseDTO.of(product);
    }

    //DELETE
    public ProductDeleteResponseDTO deleteProduct(Long productId, User user, Long category_id,
            String parentCategory_name) {

        Category category = categoryService.getCategory(category_id, parentCategory_name);
        Product product = getUserProduct(productId, user);
        if (!category.getId().equals(product.getCategory().getId())) {
            throw new IllegalArgumentException("카테고리와 Product의 카테고리가 일치하지 않습니다.");
        }

            productRepository.delete(product);
            return ProductDeleteResponseDTO.of(product);
    }

    //Product 존재여부 확인
    public Product findProduct(Long productId) {
        //RuntimeException으로 변경 예정
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Product입니다."));
    }

    //Product 본인 인증
    public Product getUserProduct(Long productId, User user) {
        Product product = findProduct(productId);
        //RuntimeException으로 변경 예정
        if (!user.getId().equals(product.getUser().getId())) {
            throw new RejectedExecutionException("본인만 수정할 수 있습니다.");
        }
        return product;
    }
}
