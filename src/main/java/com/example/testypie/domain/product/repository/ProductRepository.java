package com.example.testypie.domain.product.repository;

import com.example.testypie.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAllByCategory_id(Long childCategory_id, PageRequest id);
}

