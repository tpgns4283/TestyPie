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

    public CommentResponseDTO productComment(Long productId, CommentRequestDTO req, User user) {
        Product product = productService.findProduct(productId);
        Comment comment = new Comment(req, user, product);
        Comment saveComment = commentRepository.save(comment);
        return new CommentResponseDTO(saveComment);
    }

    public List<CommentResponseDTO> getComments(Long productId) {
        Product product = productService.findProduct(productId);

        List<Comment> comments = product.getCommentList();
        System.out.println("================================="  + comments);

        return commentRepository.findAllByProduct(product)
                .stream().map(CommentResponseDTO::new).toList();
    }

    @Transactional
    public CommentResponseDTO updateComment(Long productId, Long commentId, CommentRequestDTO req, User user) {
        Comment comment = getCommentEntity(commentId);
        checkProduct(comment, productId);
        checkUser(comment, user.getId());

        Product product = productService.findProduct(productId);
        comment.update(req, product);
        return new CommentResponseDTO(comment);
    }

    public void deleteComment(Long productId, Long commentId, User user) {
        Comment comment = getCommentEntity(commentId);
        checkProduct(comment, productId);
        checkUser(comment, user.getId());
        commentRepository.delete(comment);
    }

    // comment id로 댓글 조회
    @Transactional(readOnly = true)
    public Comment getCommentEntity(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
            () -> new IllegalArgumentException("comment id")
        );
    }

    // 게시글에 달린 댓글이 맞는지 확인
    public void checkProduct(Comment comment, Long productId) {
        if (!comment.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("comment's productId");
        }
    }

    // 수정 삭제 user 일치 확인
    public void checkUser(Comment comment, Long id) {
        if(!comment.getUser().getId().equals(id)) {
            throw new IllegalArgumentException("comment's modifier");
        }
    }
}
