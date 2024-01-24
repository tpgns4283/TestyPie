package com.example.testypie.domain.user.controller;


import com.example.testypie.domain.user.dto.LoginRequestDTO;
import com.example.testypie.domain.user.dto.MessageDTO;
import com.example.testypie.domain.user.dto.SignUpRequestDTO;
import com.example.testypie.domain.user.service.UserService;
import com.example.testypie.global.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {

    private final UserService userService;

    private final JwtUtil jwtUtil;

    //회원가입
    @PostMapping("/api/users/signup")
    public ResponseEntity<MessageDTO> signup(@Valid @RequestBody SignUpRequestDTO req) {

        userService.signup(req);

        return ResponseEntity.status(HttpStatus.CREATED.value())
            .body(new MessageDTO("회원가입 성공", HttpStatus.CREATED.value()));
    }

    //로그인
    @PostMapping("/api/users/login")
    public ResponseEntity<MessageDTO> login(@RequestBody LoginRequestDTO req,
        HttpServletResponse res) {
        userService.login(req);

        // access token, refresh token 생성
        res.setHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createAccessToken(req.account()));
        res.setHeader(JwtUtil.REFRESH_AUTHORIZATION_HEADER, jwtUtil.createRefreshToken(req.account()));

        return ResponseEntity.ok().body(new MessageDTO("로그인 성공", HttpStatus.OK.value()));
    }

    /*localStorage에 있는 JWT토큰을 Header에 실어 서버에서 토큰을 통해 유저의 아이디값을 추출하고 HashMap을 통해
     json형식의 데이터르 매핑 후 response를 다시 front에 던져주는 로직입니다.
     */
    @GetMapping("/api/user/profile")
    public Map<String, String> getUserProfile(@RequestHeader("Authorization") String authorizationHeader) {
        Map<String, String> response = new HashMap<>();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtUtil.validateToken(token)) {
                Claims info = jwtUtil.getUserInfoFromToken(token);
                String account = info.getSubject();
                response.put("account", account);
                return response;
            }
        }
        response.put("error", "유효하지 않은 토큰입니다.");
        return response;
    }
}