package com.example.testypie.domain.user.service;

import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.user.dto.ParticipatedProductResponseDTO;
import com.example.testypie.domain.user.dto.ProfileRequestDTO;
import com.example.testypie.domain.user.dto.ProfileResponseDTO;
import com.example.testypie.domain.user.dto.RegisteredProductResponseDTO;
import com.example.testypie.domain.user.entity.User;
import com.example.testypie.domain.user.repository.UserRepository;
import com.example.testypie.domain.user.dto.ProfileRequestDTO;
import com.example.testypie.domain.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
        return new ProfileResponseDTO(user.getNickname(), user.getDescription(), user.getFileUrl());
    }

   public User findProfile(String account) {
        return userRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다."));
    }

    // 프로필 작성자와 사이트 이용자가 일치하는 메서드
    public void checkSameUser(String profileAccount, String userAccount) {
        User profileUser = findProfile(profileAccount);
        User user = findProfile(userAccount);
        if (!profileUser.equals(user)) {
            throw new IllegalArgumentException("요청하는 유저에게 권한이 없습니다.");
        }

    }

    // product 등록 이력 가져오기
    public List<RegisteredProductResponseDTO> getUserProducts(String account) {
        List<Product> productList = userRepository.getUserProductsOrderByCreatedAtDesc(account);
        return productList.stream()
                .map(RegisteredProductResponseDTO::of)
                .collect(Collectors.toList());
    }

    //product 참여 이력 가져오기
    public List<ParticipatedProductResponseDTO> getUserFeedback(String account) {
        return userRepository.getUserFeedbacksDtoIncludingProductInfo(account);
    }
}
