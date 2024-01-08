package com.example.testypie.product.service;

import com.example.testypie.product.dto.ProductCreateRequestDTO;
import com.example.testypie.product.dto.ProductCreateResponseDTO;
import com.example.testypie.product.entity.Product;
import com.example.testypie.product.repositoy.ProductRepository;
import com.example.testypie.user.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductCreateResponseDTO createPost(User user, ProductCreateRequestDTO req) {

        Product product = Product.builder().user(user).title(req.title()).content(req.content()).createAt(LocalDateTime.now())
                .startedAt(req.startAt()).closedAt(req.closedAt()).build();

        Product saveProduct = productRepository.save(product);

        return ProductCreateResponseDTO.of(saveProduct);
    }
}
