package com.example.testypie.domain.comment.repository;

import com.example.testypie.domain.comment.entity.Comment;
import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByProduct(Product product);

    Optional<Comment> findByProduct(Product product);

    Optional<Comment> findByUser(User user);
}
