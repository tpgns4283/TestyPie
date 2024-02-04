package com.example.testypie.domain.product.repository;

import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.product.entity.QProduct;
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
  public Page<Product> searchAllByKeyword(
      Pageable pageable, String parentCategory_name, Long childCategory_id, String keyword) {

    QProduct product = QProduct.product;

    List<Product> contents =
        queryFactory
            .selectFrom(product)
            .where(
                product
                    .category
                    .parent
                    .name
                    .eq(parentCategory_name)
                    .and(product.category.id.eq(childCategory_id))
                    .and(
                        product
                            .title
                            .containsIgnoreCase(keyword)
                            .or(product.user.nickname.containsIgnoreCase(keyword))))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .orderBy(product.createdAt.desc())
            .fetch();

    long contentCnt =
        queryFactory
            .selectFrom(product)
            .where(
                product
                    .category
                    .parent
                    .name
                    .eq(parentCategory_name)
                    .and(product.category.id.eq(childCategory_id))
                    .and(
                        product
                            .title
                            .containsIgnoreCase(keyword)
                            .or(product.user.nickname.containsIgnoreCase(keyword))))
            .fetchCount();

    return new PageImpl<>(contents, pageable, contentCnt);
  }
}
