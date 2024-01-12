package com.example.testypie.domain.user.service;

import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.user.dto.ProfileResponseDTO;
import com.example.testypie.domain.user.dto.RegisteredProductResponseDTO;
import com.example.testypie.domain.user.entity.User;
import com.example.testypie.domain.user.repository.UserRepository;
import com.example.testypie.domain.user.dto.ProfileRequestDTO;
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

    @Transactional
    public ProfileResponseDTO updateProfile(String account, ProfileRequestDTO req) {
        User profileUser = userRepository.findByAccount(account)
                .orElseThrow(NoSuchElementException::new);
        profileUser.update(req);
        return new ProfileResponseDTO(profileUser.getNickname(), profileUser.getDescription());
    }

    public ProfileResponseDTO getProfile(String account) {
        User user = findProfile(account);
        return new ProfileResponseDTO(user.getNickname(), user.getDescription());
    }

    public User findProfile(String account) {
        return userRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다."));
    }

    // 프로필 작성자와 사이트 이용자가 일치하는 메서드
    public void checkSameUser(String profileAccount, String userAccount) {
        User profileUser = findProfile(profileAccount);
        User user = findProfile(userAccount);
        if(!profileUser.equals(user)){
            throw new IllegalArgumentException("요청하는 유저에게 권한이 없습니다.");
        }

    }

    public List<RegisteredProductResponseDTO> getUserProducts(String account) {
        List<Product> registeredProductList = userRepository.getUserProductsOrderByCreatedAtDesc(account);
        return registeredProductList.stream()
                .map(product -> new RegisteredProductResponseDTO(product.getId(), product.getTitle(), product.getCreateAt(), product.getClosedAt()))
                .collect(Collectors.toList());
    }
}
