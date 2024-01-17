package com.example.testypie.domain.product.service;

import com.example.testypie.domain.category.entity.Category;
import com.example.testypie.domain.category.service.CategoryService;
import com.example.testypie.domain.product.dto.*;
import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.product.repositoy.ProductRepository;
import com.example.testypie.domain.user.entity.User;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    //CREATE
    public ProductCreateResponseDTO createPost(User user, ProductCreateRequestDTO req, String parentCategory_name, Long category_id) {

        Category category = categoryService.getCategory(category_id, parentCategory_name);

        Product product = Product.builder().user(user).title(req.title()).content(req.content()).category(category).createAt(LocalDateTime.now())
                .startedAt(req.startAt()).closedAt(req.closedAt()).build();

        Product saveProduct = productRepository.save(product);

        return ProductCreateResponseDTO.of(saveProduct);
    }

    //READ
    public ProductReadResponseDTO getProduct(Long productId, Long category_id, String parentCategory_name) {
        Category category = categoryService.getCategory(category_id, parentCategory_name);
        Product product = findProduct(productId);

        if(category.getId().equals(product.getCategory().getId())) {
            return ProductReadResponseDTO.of(product);
        }else{
            throw new IllegalArgumentException("카테고리와 상품카테고리가 일치하지 않습니다.");
        }
    }

    public Page<ProductReadResponseDTO> getProductPage(Pageable pageable, String parentCategory_name) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 10;

        Page<Product> productPage = productRepository.findAll(
                PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        return getProductReadResponseDTOS(pageable, productPage);
    }

    public Page<ProductReadResponseDTO> getProductCategoryPage(Pageable pageable, Long childCategory_id, String parentCategory_name) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 10;

        Category category = categoryService.getCategory(childCategory_id, parentCategory_name);

        Page<Product>  productPage = productRepository.findAllByCategory_id(category.getId(),
                PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));


        return getProductReadResponseDTOS(pageable, productPage);
    }

    private Page<ProductReadResponseDTO> getProductReadResponseDTOS(Pageable pageable, Page<Product> productPage) {
        List<ProductReadResponseDTO> resList = new ArrayList<>();

        for (Product product : productPage) {
            ProductReadResponseDTO res = ProductReadResponseDTO.of(product);
            resList.add(res);
        }
        return new PageImpl<>(resList, pageable, productPage.getTotalElements());
    }

    //UPDATE
    public ProductUpdateResponseDTO updateProduct(Long productId, ProductUpdateRequestDTO req, User user, Long category_id, String parentCategory_name) {
        Product product = getUserProduct(productId, user);
        Category category = categoryService.getCategory(category_id, parentCategory_name);

        if(category.getId().equals(product.getCategory().getId())) {
            product.updateTitle(req.title());
            product.updateContent(req.content());
            product.updateCategory(category);
            product.updateModifiedAt(LocalDateTime.now());
            product.updateStartAt(req.startAt());
            product.updateClosedAt(req.closedAt());

            productRepository.save(product);

            return ProductUpdateResponseDTO.of(product);
        }else{
            throw new IllegalArgumentException("카테고리와 상품카테고리가 일치하지 않습니다.");
        }
    }

    //DELETE
    public ProductDeleteResponseDTO deleteProduct(Long productId, User user, Long category_id, String parentCategory_name) {
        Category category = categoryService.getCategory(category_id, parentCategory_name);
        Product product = getUserProduct(productId, user);

        if(category.getId().equals(product.getCategory().getId())) {
            productRepository.delete(product);
            return ProductDeleteResponseDTO.of(product);
        }else{
            throw new IllegalArgumentException("카테고리와 상품카테고리가 일치하지 않습니다.");
        }
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
        if(!user.getId().equals(product.getUser().getId())) {
            throw new RejectedExecutionException("본인만 수정할 수 있습니다.");
        }
        return product;
    }
}
