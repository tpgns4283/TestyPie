package com.example.testypie.domain.user.controller;

import com.example.testypie.global.jwt.JwtUtil;
import com.example.testypie.domain.user.dto.*;
import com.example.testypie.domain.user.entity.User;
import com.example.testypie.domain.user.service.UserInfoService;
import com.example.testypie.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
