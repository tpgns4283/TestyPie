package com.example.testypie.domain.user.service;

import com.example.testypie.domain.user.entity.RefreshToken;
import com.example.testypie.domain.user.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenService {
  private final RefreshTokenRepository refreshTokenRepository;

  public void addRefreshToken(RefreshToken refreshToken) {
    refreshTokenRepository.save(refreshToken);
  }

  // 1. 토큰이 저장되어있는지 확인
  public RefreshToken findToken(String token) {
    return refreshTokenRepository.findByTokenValue(token).orElseThrow(null);
  }

  public void deleteRefreshToken(RefreshToken refreshToken) {
    refreshTokenRepository.delete(refreshToken);
  }
}
