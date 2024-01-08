package com.example.testypie.comment.service;

import com.example.testypie.comment.dto.CommentRequestDTO;
import com.example.testypie.comment.dto.CommentResponseDTO;
import com.example.testypie.comment.entity.Comment;
import com.example.testypie.comment.repository.CommentRepository;
import com.example.testypie.product.entity.Product;
import com.example.testypie.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ProductService productService;

    public CommentResponseDTO productComment(Long productId, CommentRequestDTO req, User user) {
        Product product = productService.getProductById(productId);
        Comment comment = new Comment(req, user, product);
        Comment saveComment = commentRepository.save(comment);
        return new CommentResponseDTO(saveComment);
    }

    public List<CommentResponseDTO> getComments(Long productId) {
        Product product = productService.getProductById(productId);

        return commentRepository.findAllByProduct(product)
            .stream().map(CommentResponseDTO::new).toList();
    }

    @Transactional
    public CommentResponseDTO updateComment(Long ProductId, Long commentId, CommentRequestDTO req, User user) {
        Comment comment = getCommentEntity(commentId);
        checkProduct(comment, productId);
        checkUser(comment, user.getId());

        Product product = productService.getProductById(productId);
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
            () -> new IllegalArgumentException("comment id", commentId.toString())
        );
    }

    // 게시글에 달린 댓글이 맞는지 확인
    public void checkProduct(Comment comment, Long productId) {
        if (!comment.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("comment's productId", productId.toString());
        }
    }

    // 수정 삭제 user 일치 확인
    public void checkUser(Comment comment, String idName) {
        if(!comment.getUser().getId().equals(idName)) {
            throw new IllegalArgumentException("comment's modifier", idName);
        }
    }
}
