package com.example.testypie.domain.product.service;

import static com.example.testypie.domain.product.constant.ProductConstant.DEFAULT_PRODUCT_LIKE_CNT;

import com.example.testypie.domain.category.entity.Category;
import com.example.testypie.domain.category.service.CategoryService;
import com.example.testypie.domain.product.dto.*;
import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.product.repository.ProductRepository;
import com.example.testypie.domain.reward.dto.RewardCreateRequestDTO;
import com.example.testypie.domain.reward.dto.RewardMapper;
import com.example.testypie.domain.reward.dto.RewardReadResponseDTO;
import com.example.testypie.domain.reward.entity.Reward;
import com.example.testypie.domain.user.entity.User;
import com.example.testypie.global.exception.ErrorCode;
import com.example.testypie.global.exception.GlobalExceptionHandler;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final CategoryService categoryService;

  // CREATE
  public ProductCreateResponseDTO createProduct(
      User user, ProductCreateRequestDTO req, String parentCategory_name, Long category_id) {

    Category category = categoryService.getCategory(category_id, parentCategory_name);
    validProductCommonCreateRequestDTO(req.commonCreateRequestDTO());

    LocalDateTime startAt = null;
    LocalDateTime closedAt = null;
    List<RewardCreateRequestDTO> rewardList = null;

    if (Objects.equals(parentCategory_name, "테스트게시판")) {
      validProductTestCreateRequestDTO(req.testCreateRequestDTO());

      ProductTestCreateRequestDTO testCreateRequestDTO = req.testCreateRequestDTO().get();
      rewardList = testCreateRequestDTO.rewardList();

      LocalDate startDate = LocalDate.parse(testCreateRequestDTO.startAt());
      LocalDate closedDate = LocalDate.parse(testCreateRequestDTO.closedAt());

      startAt = startDate.atStartOfDay();
      closedAt = closedDate.atStartOfDay();

      if (closedAt.isBefore(LocalDateTime.now())) {
        throw new GlobalExceptionHandler.CustomException(ErrorCode.ENDDATE_IS_BEFORE_THAN_NOW);
      }
      if (startAt.isAfter(closedAt)) {
        throw new GlobalExceptionHandler.CustomException(ErrorCode.STARTDATE_IS_AFTER_THAN_ENDDATE);
      }
    }

    Product product =
        Product.builder()
            .user(user)
            .title(req.commonCreateRequestDTO().title())
            .content(req.commonCreateRequestDTO().content())
            .category(category)
            .productLikeCnt(DEFAULT_PRODUCT_LIKE_CNT)
            .createAt(LocalDateTime.now())
            .startedAt(startAt) // 이 값은 Optional을 통해 설정될 수 있음
            .closedAt(closedAt) // 이 값도 마찬가지
            .build();

    if (rewardList != null && !rewardList.isEmpty()) {
      product.setRewardList(RewardMapper.mapToEntityList(rewardList, product));
    }

    Product savedProduct = productRepository.save(product);
    return ProductCreateResponseDTO.of(savedProduct);
  }

  private void validProductTestCreateRequestDTO(Optional<ProductTestCreateRequestDTO> req) {
    req.ifPresent(
        request -> {
          if (request.startAt().isEmpty()) {
            throw new GlobalExceptionHandler.CustomException(ErrorCode.STARTDATE_NULL_EXCEPTION);
          }
          if (request.closedAt().isEmpty()) {
            throw new GlobalExceptionHandler.CustomException(ErrorCode.CLOSEDATE_NULL_EXCEPTION);
          }
        });
  }

  private void validProductCommonCreateRequestDTO(ProductCommonCreateRequestDTO req) {
    if (req.title().isEmpty()) {
      throw new GlobalExceptionHandler.CustomException(ErrorCode.TITLE_NULL_EXCEPTION);
    }
    if (req.content().isEmpty()) {
      throw new GlobalExceptionHandler.CustomException(ErrorCode.CONTENT_NULL_EXCEPTION);
    }
  }

  // READ
  public ProductReadResponseDTO getProduct(
      Long productId, Long category_id, String parentCategory_name) throws ParseException {

    Category category = categoryService.getCategory(category_id, parentCategory_name);
    Product product = findProduct(productId);

    List<Reward> rewardList = product.getRewardList();
    List<RewardReadResponseDTO> rewardDTOList = RewardMapper.mapToDTOList(rewardList);

    if (category.getId().equals(product.getCategory().getId())) {
      return ProductReadResponseDTO.of(product, rewardDTOList);
    } else {
      throw new GlobalExceptionHandler.CustomException(ErrorCode.SELECT_PRODUCT_CATEGORY_NOT_FOUND);
    }
  }

  public Page<ProductPageResponseDTO> getProductPage(Pageable pageable, String parentCategory_name)
      throws ParseException {
    int page = pageable.getPageNumber() - 1;
    int pageLimit = 10;

    Category parentCategory = categoryService.getParentCategory(parentCategory_name);
    Long parentId = parentCategory.getId();

    Page<Product> productPage =
        productRepository.findByParentCategoryId(
            parentId, PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

    return getProductReadResponseDTOS(pageable, productPage);
  }

  public Page<ProductPageResponseDTO> getProductCategoryPage(
      Pageable pageable, Long childCategory_id, String parentCategory_name) throws ParseException {
    int page = pageable.getPageNumber() - 1;
    int pageLimit = 10;

    Category category = categoryService.getCategory(childCategory_id, parentCategory_name);

    Page<Product> productPage =
        productRepository.findAllByCategory_id(
            category.getId(), PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

    return getProductReadResponseDTOS(pageable, productPage);
  }

  public Page<ProductPageResponseDTO> getProductPageOrderByLikeDesc(Pageable pageable)
      throws ParseException {
    int page = pageable.getPageNumber() - 1;
    int pageLimit = 10;

    Page<Product> productPage =
        productRepository.findAllSortedByProductLikeCnt(
            PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

    return getProductReadResponseDTOS(pageable, productPage);
  }

  private Page<ProductPageResponseDTO> getProductReadResponseDTOS(
      Pageable pageable, Page<Product> productPage) throws ParseException {

    List<ProductPageResponseDTO> resList = new ArrayList<>();

    for (Product product : productPage) {
      ProductPageResponseDTO res = ProductPageResponseDTO.of(product);
      resList.add(res);
    }
    return new PageImpl<>(resList, pageable, productPage.getTotalElements());
  }

  // UPDATE
  public ProductUpdateResponseDTO updateProduct(
      Long productId,
      ProductUpdateRequestDTO req,
      User user,
      Long category_id,
      String parentCategory_name) {

    Product product = getUserProduct(productId, user);
    Category category = categoryService.getCategory(category_id, parentCategory_name);
    if (!category.getId().equals(product.getCategory().getId())) {
      throw new GlobalExceptionHandler.CustomException(ErrorCode.SELECT_PRODUCT_CATEGORY_NOT_FOUND);
    }

    if (req.startAt() != null && !req.closedAt().isBlank()) {
      LocalDate startDate = LocalDate.parse(req.startAt());
      LocalDate closedDate = LocalDate.parse(req.closedAt());

      LocalDateTime startAt = startDate.atStartOfDay();
      LocalDateTime closedAt = closedDate.atStartOfDay();

      if (closedAt.isBefore(LocalDateTime.now())) {
        throw new GlobalExceptionHandler.CustomException(ErrorCode.ENDDATE_IS_BEFORE_THAN_NOW);
      }
      if (startAt.isAfter(closedAt)) {
        throw new GlobalExceptionHandler.CustomException(ErrorCode.STARTDATE_IS_AFTER_THAN_ENDDATE);
      }

      product.updateStartAt(startAt);
      product.updateClosedAt(closedAt);
    }

    product.updateTitle(req.title());
    product.updateContent(req.content());
    product.updateCategory(category);
    product.updateModifiedAt(LocalDateTime.now());

    productRepository.save(product);

    return ProductUpdateResponseDTO.of(product);
  }

  // SEARCH
  public Page<SearchProductResponseDTO> searchProductList(
      Pageable pageable, Long childCategory_id, String keyword) throws ParseException {

    int page = pageable.getPageNumber() - 1;
    int pageLimit = 10;

    List<SearchProductResponseDTO> resList = new ArrayList<>();

    Page<Product> pagePage =
        productRepository.searchAllByKeyword(
            PageRequest.of(page, pageLimit, Sort.by(Direction.DESC, "id")),
            childCategory_id,
            keyword);

    for (Product product : pagePage) {
      SearchProductResponseDTO res = SearchProductResponseDTO.of(product);
      resList.add(res);
    }
    return new PageImpl<>(resList, pageable, pagePage.getTotalElements());
  }

  // DELETE
  public ProductDeleteResponseDTO deleteProduct(
      Long productId, User user, Long category_id, String parentCategory_name) {

    Category category = categoryService.getCategory(category_id, parentCategory_name);
    Product product = getUserProduct(productId, user);
    if (!category.getId().equals(product.getCategory().getId())) {
      throw new GlobalExceptionHandler.CustomException(ErrorCode.SELECT_PRODUCT_CATEGORY_NOT_FOUND);
    }

    productRepository.delete(product);
    return ProductDeleteResponseDTO.of(product);
  }

  // Product 존재여부 확인
  public Product findProduct(Long productId) {
    // RuntimeException으로 변경 예정
    return productRepository
        .findById(productId)
        .orElseThrow(
            () -> new GlobalExceptionHandler.CustomException(ErrorCode.SELECT_PRODUCT_NOT_FOUND));
  }

  // Product 본인 인증
  public Product getUserProduct(Long productId, User user) {
    Product product = findProduct(productId);
    // RuntimeException으로 변경 예정
    if (!user.getId().equals(product.getUser().getId())) {
      throw new GlobalExceptionHandler.CustomException(
          ErrorCode.PROFILE_USER_INVALID_AUTHORIZATION);
    }
    return product;
  }
}
