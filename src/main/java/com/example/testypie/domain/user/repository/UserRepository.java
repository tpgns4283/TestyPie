package com.example.testypie.domain.user.repository;

import com.example.testypie.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAccount(String account);

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);
}
