package com.example.testypie.domain.comment.service;

import com.example.testypie.domain.category.entity.Category;
import com.example.testypie.domain.category.service.CategoryService;
import com.example.testypie.domain.comment.dto.CommentRequestDTO;
import com.example.testypie.domain.comment.dto.CommentResponseDTO;
import com.example.testypie.domain.comment.entity.Comment;
import com.example.testypie.domain.comment.repository.CommentRepository;
import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.product.service.ProductService;
import com.example.testypie.domain.user.entity.User;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ProductService productService;
    private final CategoryService categoryService;

    public CommentResponseDTO productComment(Long product_id, CommentRequestDTO req, User user, Long childCategory_id, String parentCategory_name) {
        Product product = productService.findProduct(product_id);
        Category category = categoryService.getCategory(childCategory_id, parentCategory_name);

        if(category.getId().equals(product.getCategory().getId())) {
            Comment comment = new Comment(req, user, product);
            Comment saveComment = commentRepository.save(comment);
            return new CommentResponseDTO(saveComment);
        }else{
            throw new IllegalArgumentException("카테고리와 상품카테고리가 일치하지 않습니다.");
        }
    }

    public List<CommentResponseDTO> getComments(Long product_id, Long childCategory_id, String parentCategory_name) {
        Product product = productService.findProduct(product_id);
        Category category = categoryService.getCategory(childCategory_id, parentCategory_name);

        if(category.getId().equals(product.getCategory().getId())) {
            return commentRepository.findAllByProduct(product)
                    .stream().map(CommentResponseDTO::new).toList();
        }else{
            throw new IllegalArgumentException("카테고리와 상품카테고리가 일치하지 않습니다.");
        }
    }

    @Transactional
    public CommentResponseDTO updateComment(Long product_id, Long comment_id, CommentRequestDTO req, User user, Long childCategory_id, String parentCategory_name) {

        Product product = productService.findProduct(product_id);
        Category category = categoryService.getCategory(childCategory_id, parentCategory_name);

        if(category.getId().equals(product.getCategory().getId())) {
            Comment comment = getCommentEntity(comment_id);
            checkProduct(comment, product_id);
            checkUser(comment, user.getId());
            comment.update(req, product);
            return new CommentResponseDTO(comment);
        }else{
            throw new IllegalArgumentException("카테고리와 상품카테고리가 일치하지 않습니다.");
        }
    }

    public void deleteComment(Long product_id, Long comment_id, User user, Long childCategory_id, String parentCategory_name) {

        Product product = productService.findProduct(product_id);
        Category category = categoryService.getCategory(childCategory_id, parentCategory_name);

        if(category.getId().equals(product.getCategory().getId())) {
            Comment comment = getCommentEntity(comment_id);
            checkProduct(comment, product_id);
            checkUser(comment, user.getId());
            commentRepository.delete(comment);
        }else{
            throw new IllegalArgumentException("카테고리와 상품카테고리가 일치하지 않습니다.");
        }
    }

    // comment id로 댓글 조회
    @Transactional(readOnly = true)
    public Comment getCommentEntity(Long comment_id) {
        return commentRepository.findById(comment_id).orElseThrow(
                () -> new IllegalArgumentException("comment id")
        );
    }

    // 게시글에 달린 댓글이 맞는지 확인
    public void checkProduct(Comment comment, Long product_id) {
        if (!comment.getProduct().getId().equals(product_id)) {
            throw new IllegalArgumentException("comment's product_id");
        }
    }

    // 수정 삭제 user 일치 확인
    public void checkUser(Comment comment, Long user_id) {
        if(!comment.getUser().getId().equals(user_id)) {
            throw new IllegalArgumentException("comment's modifier");
        }
    }
}
