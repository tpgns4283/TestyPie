package com.example.testypie.domain.user.service;

import com.example.testypie.domain.user.entity.RefreshToken;
import com.example.testypie.domain.user.repository.RefreshTokenRepository;
import com.example.testypie.global.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    public void addRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    // 1. 토큰이 저장되어있는지 확인
    public RefreshToken findToken(String token) {
        return refreshTokenRepository.findByTokenValue(token)
                .orElseThrow(null);
    }

    // 2. 토큰에서 claims 꺼내기

    // 3. Claims의 UserId에 해당하는 회원정보 있는지 확인

    // 4. memeerId에 해당하는 회원이 있을경우 새롭게 AccessToken생성, 파라미터로 받은
    //리프래시 토큰을 같이 반환
}
