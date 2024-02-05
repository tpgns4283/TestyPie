package com.example.testypie.domain.comment.service;

import static com.example.testypie.domain.comment.constant.CommentConstant.DEFAULT_COMMENT_LIKE_CNT;

import com.example.testypie.domain.category.entity.Category;
import com.example.testypie.domain.comment.dto.request.CreateCommentRequestDTO;
import com.example.testypie.domain.comment.dto.request.UpdateCommentRequestDTO;
import com.example.testypie.domain.comment.dto.response.CreateCommentResponseDTO;
import com.example.testypie.domain.comment.dto.response.ReadPageCommentResponseDTO;
import com.example.testypie.domain.comment.dto.response.UpdateCommentResponseDTO;
import com.example.testypie.domain.comment.entity.Comment;
import com.example.testypie.domain.comment.repository.CommentRepository;
import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.user.entity.User;
import com.example.testypie.global.exception.ErrorCode;
import com.example.testypie.global.exception.GlobalExceptionHandler;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;

  public CreateCommentResponseDTO createComment(
      Category category, Product product, User user, CreateCommentRequestDTO req) {

    if (category.getId().equals(product.getCategory().getId())) {
      Comment comment =
          Comment.builder()
              .user(user)
              .content(req.content())
              .commentLikeCnt(DEFAULT_COMMENT_LIKE_CNT)
              .createAt(LocalDateTime.now())
              .product(product)
              .build();
      Comment saveComment = commentRepository.save(comment);
      return CreateCommentResponseDTO.of(saveComment);
    } else {
      throw new GlobalExceptionHandler.CustomException(ErrorCode.SELECT_COMMENT_NOT_MATCH_ORIGIN);
    }
  }

  public Page<ReadPageCommentResponseDTO> getComments(
      Pageable pageable, Category category, Product product) {
    int page = pageable.getPageNumber() - 1;
    int pageLimit = 10;

    if (category.getId().equals(product.getCategory().getId())) {
      Page<Comment> commentPage =
          commentRepository.findAllByProduct(
              product, PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

      List<ReadPageCommentResponseDTO> resList = new ArrayList<>();

      for (Comment comment : commentPage) {
        ReadPageCommentResponseDTO res = ReadPageCommentResponseDTO.of(comment);
        resList.add(res);
      }
      return new PageImpl<>(resList, pageable, commentPage.getTotalElements());
    }

    throw new GlobalExceptionHandler.CustomException(ErrorCode.SELECT_COMMENT_NOT_MATCH_ORIGIN);
  }

  @Transactional
  public UpdateCommentResponseDTO updateComment(
      Category category, Product product, User user, Long comment_id, UpdateCommentRequestDTO req) {

    if (category.getId().equals(product.getCategory().getId())) {
      Comment comment = getCommentEntity(comment_id);
      checkProduct(comment, product.getId());
      checkUser(comment, user.getId());
      comment.update(req, product);
      return UpdateCommentResponseDTO.of(comment);
    } else {
      throw new GlobalExceptionHandler.CustomException(ErrorCode.SELECT_COMMENT_NOT_MATCH_ORIGIN);
    }
  }

  public void deleteComment(Category category, Product product, User user, Long comment_id) {

    if (category.getId().equals(product.getCategory().getId())) {
      Comment comment = getCommentEntity(comment_id);
      checkProduct(comment, product.getId());
      checkUser(comment, user.getId());
      commentRepository.delete(comment);
    } else {
      throw new GlobalExceptionHandler.CustomException(ErrorCode.SELECT_COMMENT_NOT_MATCH_ORIGIN);
    }
  }

  // comment id로 댓글 조회
  @Transactional(readOnly = true)
  public Comment getCommentEntity(Long comment_id) {
    return commentRepository
        .findById(comment_id)
        .orElseThrow(
            () -> new GlobalExceptionHandler.CustomException(ErrorCode.SELECT_COMMENT_NOT_EXIST));
  }

  // 게시글에 달린 댓글이 맞는지 확인
  public void checkProduct(Comment comment, Long product_id) {
    if (!comment.getProduct().getId().equals(product_id)) {
      throw new GlobalExceptionHandler.CustomException(ErrorCode.SELECT_COMMENT_NOT_MATCH_ORIGIN);
    }
  }

  // 수정 삭제 user 일치 확인
  public void checkUser(Comment comment, Long user_id) {
    if (!comment.getUser().getId().equals(user_id)) {
      throw new GlobalExceptionHandler.CustomException(ErrorCode.SELECT_COMMENT_INVALID_USER);
    }
  }
}
