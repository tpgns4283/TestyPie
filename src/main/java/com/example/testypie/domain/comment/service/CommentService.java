package com.example.testypie.domain.comment.service;

import static com.example.testypie.domain.comment.constant.CommentConstant.DEFAULT_COMMENT_LIKE_CNT;

import com.example.testypie.domain.category.entity.Category;
import com.example.testypie.domain.comment.dto.CommentRequestDTO;
import com.example.testypie.domain.comment.dto.CommentResponseDTO;
import com.example.testypie.domain.comment.entity.Comment;
import com.example.testypie.domain.comment.repository.CommentRepository;
import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentResponseDTO createComment(Category category, Product product, User user,
            CommentRequestDTO req) {

        if (category.getId().equals(product.getCategory().getId())) {
            Comment comment = Comment.builder()
                    .user(user)
                    .content(req.content())
                    .commentLikeCnt(DEFAULT_COMMENT_LIKE_CNT)
                    .createAt(LocalDateTime.now())
                    .product(product)
                    .build();
            Comment saveComment = commentRepository.save(comment);
            return CommentResponseDTO.of(saveComment);
        } else {
            throw new IllegalArgumentException("카테고리와 상품카테고리가 일치하지 않습니다.");
        }
    }

    public Page<CommentResponseDTO> getComments(Pageable pageable, Category category,
            Product product) {

        if (category.getId().equals(product.getCategory().getId())) {
            Page<Comment> commentPage = commentRepository.findAllByProduct(product, pageable);
            List<CommentResponseDTO> resList = new ArrayList<>();

            for (Comment comment : commentPage) {
                CommentResponseDTO res = CommentResponseDTO.of(comment);
                resList.add(res);
            }

            return new PageImpl<>(resList, pageable, commentPage.getTotalElements());
        }

        throw new IllegalArgumentException("카테고리와 상품카테고리가 일치하지 않습니다.");
    }

    @Transactional
    public CommentResponseDTO updateComment(Category category, Product product, User user,
            Long comment_id, CommentRequestDTO req) {

        if (category.getId().equals(product.getCategory().getId())) {
            Comment comment = getCommentEntity(comment_id);
            checkProduct(comment, product.getId());
            checkUser(comment, user.getId());
            comment.update(req, product);
            return CommentResponseDTO.of(comment);
        } else {
            throw new IllegalArgumentException("카테고리와 상품카테고리가 일치하지 않습니다.");
        }
    }

    public void deleteComment(Category category, Product product, User user, Long comment_id) {

        if (category.getId().equals(product.getCategory().getId())) {
            Comment comment = getCommentEntity(comment_id);
            checkProduct(comment, product.getId());
            checkUser(comment, user.getId());
            commentRepository.delete(comment);
        } else {
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
        if (!comment.getUser().getId().equals(user_id)) {
            throw new IllegalArgumentException("comment's modifier");
        }
    }
}
