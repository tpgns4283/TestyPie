package com.example.testypie.domain.user.service;

import com.example.testypie.domain.user.dto.ProfileResponseDTO;
import com.example.testypie.domain.user.entity.User;
import com.example.testypie.domain.user.repository.UserRepository;
import com.example.testypie.domain.user.dto.ProfileRequestDTO;
import com.example.testypie.domain.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class UserInfoService {

    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public ProfileResponseDTO updateProfile(String account, ProfileRequestDTO req) {
        User profileUser = userRepository.findByAccount(account)
                .orElseThrow(NoSuchElementException::new);
        profileUser.update(req);
        return new ProfileResponseDTO(profileUser.getNickname(), profileUser.getDescription(),
            profileUser.getFileUrl());
    }

    public ProfileResponseDTO getProfile(String account) {
        User user = findProfile(account);
        return new ProfileResponseDTO(user.getNickname(), user.getNickname(), user.getFileUrl());
    }

   public User findProfile(String account) {
        return userRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다."));
    }
}
