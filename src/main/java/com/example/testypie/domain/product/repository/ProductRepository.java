package com.example.testypie.domain.product.repository;

import com.example.testypie.domain.product.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, SearchRepository {

  Page<Product> findAllByCategory_id(Long childCategory_id, PageRequest id);

  List<Product> findBySurveyIdIsNull();
}
