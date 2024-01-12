package com.example.testypie.user.repository;

import com.example.testypie.product.entity.Product;
import com.example.testypie.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAccount(String account);

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    @Query("SELECT p FROM Product p WHERE p.user.account = :account ORDER BY p.createAt DESC")
    List<Product> getUserProductsOrderByCreatedAtDesc(String account);
}
