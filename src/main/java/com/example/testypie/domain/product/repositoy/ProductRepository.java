package com.example.testypie.domain.product.repositoy;

import com.example.testypie.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {

}
