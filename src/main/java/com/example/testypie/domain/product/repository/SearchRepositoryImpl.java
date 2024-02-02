package com.example.testypie.domain.product.repository;

import com.example.testypie.domain.product.dto.SearchProductResponseDTO;
import com.example.testypie.domain.product.entity.QProduct;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SearchRepositoryImpl implements SearchRepository {

  private final JPQLQueryFactory queryFactory;

  @Override
  public Page<SearchProductResponseDTO> searchAllByKeyword(Pageable pageable, String keyword) {

    QProduct product = QProduct.product;

    List<SearchProductResponseDTO> contents =
        queryFactory
            .select(
                Projections.constructor(
                    SearchProductResponseDTO.class,
                    product.id,
                    product.user.account,
                    product.user.nickname,
                    product.title,
                    product.content,
                    product.category.parent.name,
                    product.category.id,
                    product.productLikeCnt,
                    product.createdAt,
                    product.startedAt,
                    product.closedAt))
            .from(product)
            .where(
                product
                    .title
                    .containsIgnoreCase(keyword)
                    .or(product.user.nickname.containsIgnoreCase(keyword)))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .orderBy(product.createdAt.desc())
            .fetch();

    long contentCnt =
        queryFactory
            .selectFrom(product)
            .where(
                product
                    .title
                    .containsIgnoreCase(keyword)
                    .or(product.user.nickname.containsIgnoreCase(keyword)))
            .fetchCount();

    return new PageImpl<>(contents, pageable, contentCnt);
  }
}
