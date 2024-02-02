package com.example.testypie.domain.user.service;

import com.example.testypie.domain.user.dto.LoginRequestDTO;
import com.example.testypie.domain.user.dto.SignUpRequestDTO;
import com.example.testypie.domain.user.dto.SignUpResponseDTO;
import com.example.testypie.domain.user.entity.User;
import com.example.testypie.domain.user.repository.UserRepository;
import com.example.testypie.global.exception.ErrorCode;
import com.example.testypie.global.exception.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

//    private final S3Uploader s3Uploader;

    @Value("${default.image.address}")
    private String defaultProfileImageUrl;

    public SignUpResponseDTO signup(SignUpRequestDTO req) {

        validateSignup(req);

        String password = passwordEncoder.encode(req.password());

        User user = User.builder().account(req.account()).password(password).email(req.email())
                .nickname(req.nickname()).description(req.description()).fileUrl(defaultProfileImageUrl)
                .build();

        userRepository.save(user);

        return new SignUpResponseDTO();
    }

    public void login(LoginRequestDTO req) {
        String account = req.account();
        String password = req.password();

        User user = userRepository.findByAccount(account)
                .orElseThrow(() -> new GlobalExceptionHandler.CustomException(ErrorCode.LOGIN_INVALID_ACCOUNT));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new GlobalExceptionHandler.CustomException(ErrorCode.LOGIN_INVALID_PASSWORD);
        }
    }

    public User findUser(String account) {
        return userRepository.findByAccount(account)
                .orElseThrow(() -> new GlobalExceptionHandler.CustomException(ErrorCode.SELECT_USER_NOT_FOUND));
    }

    public User findUserByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new GlobalExceptionHandler.CustomException(ErrorCode.SELECT_USER_NOT_FOUND));
    }

    public void signOut(User user) {
        Long userId = user.getId();

        deleteUser(userId);
    }

    private void deleteUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new GlobalExceptionHandler.CustomException(ErrorCode.DELETE_USER_NOT_FOUND));
        userRepository.deleteById(userId);
    }

    private void validateSignup(SignUpRequestDTO req) {
        if (userRepository.findByAccount(req.account()).isPresent()) {
            throw new GlobalExceptionHandler.CustomException(ErrorCode.SIGNUP_DUPLICATED_USER_ACCOUNT);
        }
        if (userRepository.findByEmail(req.email()).isPresent()) {
            throw new GlobalExceptionHandler.CustomException(ErrorCode.SIGNUP_DUPLICATED_USER_EMAIL);
        }
        if (userRepository.findByNickname(req.nickname()).isPresent()) {
            throw new GlobalExceptionHandler.CustomException(ErrorCode.SIGNUP_DUPLICATED_USER_NICKNAME);
        }
    }

}
