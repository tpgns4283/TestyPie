package com.example.testypie.domain.user.kakao.service;

import com.example.testypie.domain.user.entity.RefreshToken;
import com.example.testypie.domain.user.entity.User;
import com.example.testypie.domain.user.kakao.dto.KakaoUserInfoDto;
import com.example.testypie.domain.user.repository.RefreshTokenRepository;
import com.example.testypie.domain.user.repository.UserRepository;
import com.example.testypie.global.jwt.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j(topic = "KAKAO LOGIN")
@Service
@RequiredArgsConstructor
public class KakaoService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public List<String> kakaoLogin(String code) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String kakaoAccessToken = getToken(code);

        // 2. 토큰으로 카카오 API호출: "액세스 토큰"으로 카카오사용자 정보 가져오기
        KakaoUserInfoDto kakaoUserInfoDto = getKakaoUserInfo(kakaoAccessToken);

        // 3. 회원가입 처리
        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfoDto);

        // 4. access 토큰, refresh 토큰 발급
        String accessToken = jwtUtil.createAccessToken(kakaoUser.getAccount());
        String refreshToken = jwtUtil.createRefreshToken(kakaoUser.getAccount());

        List<String> tokens = new ArrayList<>();
        tokens.add(accessToken);
        tokens.add(refreshToken);

        // 카카오 유저 Refresh토큰 저장하기
        RefreshToken saveRefreshToken =
                RefreshToken.builder().account(kakaoUser.getAccount()).tokenValue(refreshToken).build();

        refreshTokenRepository.save(saveRefreshToken);

        return tokens;
    }

    private String getToken(String code) throws JsonProcessingException {
        log.info("인가코드: " + code);

        // 요청 URI 만들기
        URI uri =
                UriComponentsBuilder.fromUriString("https://kauth.kakao.com")
                        .path("/oauth/token")
                        .encode()
                        .build()
                        .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "58c7ccd47e3b71e699a7c7d0c56341e5");
        body.add("redirect_uri", "https://testypie.link/kakao-login/callback");
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity =
                RequestEntity.post(uri).headers(headers).body(body);

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());

        log.info(jsonNode.get("token_type").asText());
        log.info(jsonNode.get("access_token").asText());
        log.info(jsonNode.get("expires_in").asText());
        log.info(jsonNode.get("refresh_token").asText());
        log.info(jsonNode.get("refresh_token_expires_in").asText());

        return jsonNode.get("access_token").asText();
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // 요청 URL 만들기
        URI uri =
                UriComponentsBuilder.fromUriString("https://kapi.kakao.com")
                        .path("/v2/user/me")
                        .encode()
                        .build()
                        .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity =
                RequestEntity.post(uri).headers(headers).body(new LinkedMultiValueMap<>());

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties").get("nickname").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();

        log.info("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);
        return new KakaoUserInfoDto(id, nickname, email);
    }

    private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.getId();
        User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);

        if (kakaoUser == null) {
            // 카카오 사용자 email 동일한 email 가진 회원이 있는지 확인
            String kakaoEmail = kakaoUserInfo.getEmail();
            User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);
            if (sameEmailUser != null) {
                kakaoUser = sameEmailUser;
                // 기존 회원정보에 카카오 Id 추가
                kakaoUser = kakaoUser.kakaoIdUpdate(kakaoId);
            } else {
                // 신규 회원가입
                // password: random UUID
                String password = UUID.randomUUID().toString();
                String account = kakaoUserInfo.getNickname();
                String encodedPassword = passwordEncoder.encode(password);

                // email: kakao email
                String email = kakaoUserInfo.getEmail();
                String nickname = kakaoUserInfo.getNickname();

                kakaoUser =
                        User.builder()
                                .account(account)
                                .password(encodedPassword)
                                .email(email)
                                .nickname(nickname)
                                .kakaoId(kakaoId)
                                .build();
            }

            userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }
}
