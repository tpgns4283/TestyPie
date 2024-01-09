package com.example.testypie.comment.service;

import com.example.testypie.comment.dto.CommentRequestDTO;
import com.example.testypie.comment.dto.CommentResponseDTO;
import com.example.testypie.comment.entity.Comment;
import com.example.testypie.comment.repository.CommentRepository;
import com.example.testypie.product.entity.Product;
import com.example.testypie.product.service.ProductService;
import com.example.testypie.user.entity.User;

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

    public CommentResponseDTO productComment(Long product_id, CommentRequestDTO req, User user) {
        Product product = productService.findProduct(product_id);
        Comment comment = new Comment(req, user, product);
        Comment saveComment = commentRepository.save(comment);
        return new CommentResponseDTO(saveComment);
    }

    public List<CommentResponseDTO> getComments(Long product_id) {
        Product product = productService.findProduct(product_id);
        return commentRepository.findAllByProduct(product)
                .stream().map(CommentResponseDTO::new).toList();
    }

    @Transactional
    public CommentResponseDTO updateComment(Long product_id, Long comment_id, CommentRequestDTO req, User user) {
        Comment comment = getCommentEntity(comment_id);
        checkProduct(comment, product_id);
        checkUser(comment, user.getId());

        Product product = productService.findProduct(product_id);
        comment.update(req, product);
        return new CommentResponseDTO(comment);
    }

    public void deleteComment(Long product_id, Long comment_id, User user) {
        Comment comment = getCommentEntity(comment_id);
        checkProduct(comment, product_id);
        checkUser(comment, user.getId());
        commentRepository.delete(comment);
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
