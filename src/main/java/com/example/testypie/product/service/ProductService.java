package com.example.testypie.product.service;

import com.example.testypie.product.dto.*;
import com.example.testypie.product.entity.Product;
import com.example.testypie.product.repositoy.ProductRepository;
import com.example.testypie.user.entity.User;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //CREATE
    public ProductCreateResponseDTO createPost(User user, ProductCreateRequestDTO req) {

        Product product = Product.builder().user(user).title(req.title()).content(req.content()).createAt(LocalDateTime.now())
                .startedAt(req.startAt()).closedAt(req.closedAt()).build();

        Product saveProduct = productRepository.save(product);

        return ProductCreateResponseDTO.of(saveProduct);
    }

    //READ
    public ProductReadResponseDTO getProduct(Long productId) {
        Product product = findProduct(productId);
        return ProductReadResponseDTO.of(product);
    }

    //UPDATE
    public ProductUpdateResponseDTO updateProduct(Long productId, ProductUpdateRequestDTO req, User user) {
        Product product = getUserProduct(productId, user);

        product.updateTitle(req.title());
        product.updateContent(req.content());
        product.updateModifiedAt(LocalDateTime.now());
        product.updateStartAt(req.startAt());
        product.updateClosedAt(req.closedAt());

        productRepository.save(product);

        return ProductUpdateResponseDTO.of(product);
    }

    //DELETE
    public ProductDeleteResponseDTO deleteProduct(Long productId, User user) {
        Product product = getUserProduct(productId, user);
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
        if(!user.getId().equals(product.getUser().getId())) {
            throw new RejectedExecutionException("본인만 수정할 수 있습니다.");
        }
        return product;
    }

    public Page<ProductReadResponseDTO> getProductPage(Pageable pageable) {
        int page = pageable.getPageNumber()-1;
        int pageLimit = 10; //한 페이지에 게시글 10개

        //ID기준 내림차순
        Page<Product> productPage = productRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        List<ProductReadResponseDTO> resList = new ArrayList<>();

        for (Product product : productPage) {
            ProductReadResponseDTO res = ProductReadResponseDTO.of(product);
            resList.add(res);
        }

        return new PageImpl<>(resList, pageable, productPage.getTotalElements());
    }
}
