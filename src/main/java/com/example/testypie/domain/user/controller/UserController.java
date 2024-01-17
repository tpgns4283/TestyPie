package com.example.testypie.domain.user.controller;


import com.example.testypie.domain.user.dto.LoginRequestDTO;
import com.example.testypie.domain.user.dto.MessageDTO;
import com.example.testypie.domain.user.dto.SignUpRequestDTO;
import com.example.testypie.domain.user.service.UserService;
import com.example.testypie.global.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

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
}
