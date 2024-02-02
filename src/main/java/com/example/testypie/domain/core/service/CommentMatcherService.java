package com.example.testypie.domain.core.service;

import com.example.testypie.domain.category.entity.Category;
import com.example.testypie.domain.category.service.CategoryService;
import com.example.testypie.domain.comment.dto.CommentRequestDTO;
import com.example.testypie.domain.comment.dto.CommentResponseDTO;
import com.example.testypie.domain.comment.service.CommentService;
import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.product.service.ProductService;
import com.example.testypie.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentMatcherService {

    private final CommentService commentService;
    private final ProductService productService;
    private final CategoryService categoryService;

    public CommentResponseDTO productComment(
            Long product_id,
            CommentRequestDTO req,
            User user,
            Long childCategory_id,
            String parentCategory_name) {

        Product product = productService.findProduct(product_id);
        Category category = categoryService.getCategory(childCategory_id, parentCategory_name);

        return commentService.createComment(category, product, user, req);
    }

    public Page<CommentResponseDTO> getComments(
            Pageable pageable, Long product_id, Long childCategory_id, String parentCategory_name) {

        Product product = productService.findProduct(product_id);
        Category category = categoryService.getCategory(childCategory_id, parentCategory_name);

        return commentService.getComments(pageable, category, product);
    }

    @Transactional
    public CommentResponseDTO updateComment(
            Long product_id,
            Long comment_id,
            CommentRequestDTO req,
            User user,
            Long childCategory_id,
            String parentCategory_name) {

        Product product = productService.findProduct(product_id);
        Category category = categoryService.getCategory(childCategory_id, parentCategory_name);

        return commentService.updateComment(category, product, user, comment_id, req);
    }

    public void deleteComment(
            Long product_id,
            Long comment_id,
            User user,
            Long childCategory_id,
            String parentCategory_name) {

        Product product = productService.findProduct(product_id);
        Category category = categoryService.getCategory(childCategory_id, parentCategory_name);
        commentService.deleteComment(category, product, user, comment_id);
    }
}
