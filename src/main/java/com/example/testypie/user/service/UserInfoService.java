package com.example.testypie.user.service;

import com.example.testypie.user.dto.ProfileRequestDTO;
import com.example.testypie.user.dto.ProfileResponseDTO;
import com.example.testypie.user.entity.User;
import com.example.testypie.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class UserInfoService {

    private final UserRepository userRepository;

    @Transactional
    public ProfileResponseDTO updateProfile(String account, ProfileRequestDTO req) {
        User profileUser = userRepository.findByAccount(account)
                .orElseThrow(NoSuchElementException::new);
        profileUser.update(req);
        return new ProfileResponseDTO(profileUser.getNickname(), profileUser.getDescription());
    }

    public ProfileResponseDTO getProfile(String account) {
        User user = findProfile(account);
        return new ProfileResponseDTO(user.getNickname(), user.getNickname());
    }

    public User findProfile(String account) {
        return userRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다."));
    }
}
