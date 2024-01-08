package com.example.testypie.user.service;

import com.example.testypie.user.entity.User;
import com.example.testypie.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserInfoService {

    private final UserRepository userRepository;

    public User getUserByAccount(String account) {
        return userRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다."));
    }
}
