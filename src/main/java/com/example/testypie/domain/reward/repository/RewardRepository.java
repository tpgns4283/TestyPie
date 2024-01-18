package com.example.testypie.domain.reward.repository;

import com.example.testypie.domain.reward.entity.Reward;
import com.example.testypie.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;


public interface RewardRepository extends JpaRepository<Reward, Long> {
    List<Reward> findAllByUser(User user);
}
