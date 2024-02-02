package com.example.testypie.domain.product.repository;

import com.example.testypie.domain.product.dto.SearchProductResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository {

  Page<SearchProductResponseDTO> searchAllByKeyword(Pageable pageable, String keyWord);
}
