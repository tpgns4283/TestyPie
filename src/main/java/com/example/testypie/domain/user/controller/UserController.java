package com.example.testypie.domain.user.controller;

import com.example.testypie.global.jwt.JwtUtil;
import com.example.testypie.domain.user.dto.*;
import com.example.testypie.domain.user.entity.User;
import com.example.testypie.domain.user.service.UserInfoService;
import com.example.testypie.domain.user.service.UserService;
import com.example.testypie.global.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final UserInfoService userInfoService;

    private final JwtUtil jwtUtil;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<MessageDTO> signup(@Valid @RequestBody SignUpRequestDTO req) {

        userService.signup(req);

        return ResponseEntity.status(HttpStatus.CREATED.value())
                .body(new MessageDTO("회원가입 성공", HttpStatus.CREATED.value()));
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<MessageDTO> login(@RequestBody LoginRequestDTO req,
            HttpServletResponse res) {
        userService.login(req);
        res.setHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(req.account()));
        return ResponseEntity.ok().body(new MessageDTO("로그인 성공", HttpStatus.OK.value()));
    }

    //프로필 조회
    @GetMapping("/{account}")
    public ResponseEntity<ProfileResponseDTO> getProfile(@PathVariable String account) {
        User user = userInfoService.findProfile(account);
        return ResponseEntity.ok().body(ProfileResponseDTO.of(user));
    }

    //프로필 수정
    @PatchMapping("/{account}")
    public ResponseEntity<?> updateProfile(@PathVariable String account,
            @RequestBody ProfileRequestDTO req) {

        try {
            ProfileResponseDTO res = userInfoService.updateProfile(account, req);
            return ResponseEntity.ok(res);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new MessageDTO("업데이트에 실패했습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

    // 2024-01-12
    // product 등록 이력 조회
    // user가 자신의 프로필에서 product 등록이력을 조회하는 서비스입니다.
    // 로직은 아래와 같습니다.
    // 1. 유효한 사용자인지 체크합니다.
    // 2. 유효한 사용자일 경우 등록한 product를 전부 가져옵니다.
    // 3. RegisteredProductResponstDTO객체는 productId, product title, createdAt, closedAt를 가집니다.
    @GetMapping("/{account}/registeredProducts")
    public ResponseEntity<List<RegisteredProductResponseDTO>> getRegisteredProducts(@PathVariable String account, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 1.
        userInfoService.checkSameUser(account, userDetails.getUsername());

        // 2, 3.
        List<RegisteredProductResponseDTO> res = userInfoService.getUserProducts(account);

        return ResponseEntity.ok().body(res);
    }

    // 2024-01-15
    // product 참여 이력 조회
    // user가 자신의 프로필에서 product 등록 이력을 조회하는 서비스입니다.
    // 로직은 아래와 같습니다.
    // 1. 유효한 사용자인지 체크합니다.
    // 2. 해당 유저가 작성한 feedback을 모두 조회합니다.
    // 3. 조회한 feedback들에서 product이름, **feedback별점(미구현), feedback 작성일시를 가져옵니다.

    @GetMapping("{account}/participatedProducts")
    public ResponseEntity<List<ParticipatedProductResponseDTO>> getParticipatedProducts(@PathVariable String account, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 1.
        userInfoService.checkSameUser(account, userDetails.getUsername());

        // 2. 3.
        List<ParticipatedProductResponseDTO> res = userInfoService.getUserFeedback(account);

        return ResponseEntity.ok().body(res);
    }
}
