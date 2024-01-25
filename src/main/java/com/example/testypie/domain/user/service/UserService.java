package com.example.testypie.domain.user.service;

import com.example.testypie.domain.user.dto.LoginRequestDTO;
import com.example.testypie.domain.user.repository.UserRepository;
import com.example.testypie.domain.user.dto.SignUpRequestDTO;
import com.example.testypie.domain.user.entity.User;
import com.example.testypie.domain.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final S3Uploader s3Uploader;

    public void signup(SignUpRequestDTO signUpRequestDTO) {

        String account = signUpRequestDTO.account();
        String password = passwordEncoder.encode(signUpRequestDTO.password());
        String email = signUpRequestDTO.email();
        String nickname = signUpRequestDTO.nickname();
        String description = signUpRequestDTO.description();

        if (userRepository.findByAccount(account).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        User user = User.builder().account(account).password(password).email(email)
                .nickname(nickname).description(description).build();

        userRepository.save(user);
    }

    public void login(LoginRequestDTO req) {
        String account = req.account();
        String password = req.password();

        User user = userRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    public void signOut(User user) {
        Long userId = user.getId();

        deleteUser(userId);
    }

    private void deleteUser(Long userId) {
        userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("유저를 확인하지 못했습니다."));
        userRepository.deleteById(userId);
    }
}
