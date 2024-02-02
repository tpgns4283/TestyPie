package com.example.testypie.domain.user.controller;

import static com.example.testypie.global.jwt.JwtUtil.*;

import com.example.testypie.domain.user.dto.LoginRequestDTO;
import com.example.testypie.domain.user.dto.MessageDTO;
import com.example.testypie.domain.user.dto.SignUpRequestDTO;
import com.example.testypie.domain.user.entity.RefreshToken;
import com.example.testypie.domain.user.entity.User;
import com.example.testypie.domain.user.service.RefreshTokenService;
import com.example.testypie.domain.user.service.UserService;
import com.example.testypie.global.exception.ErrorCode;
import com.example.testypie.global.exception.GlobalExceptionHandler;
import com.example.testypie.global.jwt.JwtUtil;
import com.example.testypie.global.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

  private final RefreshTokenService refreshTokenService;

  private final UserService userService;

  private final JwtUtil jwtUtil;

  // 회원가입
  @PostMapping("/api/users/signup")
  public ResponseEntity<MessageDTO> signup(
      @RequestBody @Valid SignUpRequestDTO req, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new GlobalExceptionHandler.CustomException(ErrorCode.SIGNUP_DUPLICATED_USER_ACCOUNT);
    }

    userService.signup(req);

    return ResponseEntity.status(HttpStatus.CREATED.value())
        .body(new MessageDTO("회원가입 성공", HttpStatus.CREATED.value()));
  }

  // 로그인
  @PostMapping("/api/users/login")
  public ResponseEntity<MessageDTO> login(
      @RequestBody @Valid LoginRequestDTO req,
      BindingResult bindingResult,
      HttpServletResponse res) {
    if (bindingResult.hasErrors()) {
      throw new GlobalExceptionHandler.CustomException(ErrorCode.SELECT_USER_NOT_FOUND);
    }

    userService.login(req);

    // access token 생성(header에 관리)
    res.setHeader(AUTHORIZATION_HEADER, jwtUtil.createAccessToken(req.account()));

    // refresh token 값 생성(cookie에 관리)
    String refreshTokenValue = jwtUtil.createRefreshToken(req.account());

    // refresh token 객체생성
    RefreshToken refreshToken =
        RefreshToken.builder().account(req.account()).tokenValue(refreshTokenValue).build();

    // 쿠키생성
    Cookie cookie = new Cookie(REFRESH_AUTHORIZATION_HEADER, refreshTokenValue);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    res.addCookie(cookie);

    // 쿠키 저장
    refreshTokenService.addRefreshToken(refreshToken);

    return ResponseEntity.ok().body(new MessageDTO("로그인 성공", HttpStatus.OK.value()));
  }

  /*localStorage에 있는 JWT토큰을 Header에 실어 서버에서 토큰을 통해 유저의 아이디값을 추출하고 HashMap을 통해
  json형식의 데이터르 매핑 후 response를 다시 front에 던져주는 로직입니다.
  */
  @GetMapping("/api/user/profile")
  public Map<String, String> getUserProfile(
      @RequestHeader("Authorization") String authorizationHeader) {
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

  @DeleteMapping("/api/users/signout")
  public ResponseEntity<MessageDTO> signOut(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    User user = userDetails.getUser();
    userService.signOut(user);
    return ResponseEntity.ok(new MessageDTO("유저가 탈퇴했습니다.", 200));
  }

  // 1. 토큰으로 refresh token을 찾는다.
  // 2. 만약 refresh token이 비어있다면, 토큰이 유효하지 않습니다.
  // 3. 액세스토큰과 리프레시 토큰을 다시 만들어 준다.

  @PostMapping("/api/users/refresh")
  public ResponseEntity<?> refresh(
      @CookieValue(REFRESH_AUTHORIZATION_HEADER) String token, HttpServletResponse res) {

    RefreshToken refreshToken = refreshTokenService.findToken(token);

    if (refreshToken == null) {
      // 리프레시 토큰이 존재하지 않는 경우에 대한 처리
      throw new GlobalExceptionHandler.CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
    }

    Claims claims = jwtUtil.getUserInfoFromToken(refreshToken.getTokenValue());

    if (claims == null) {
      // 리프레시 토큰이 유효하지 않거나 사용자 ID를 포함하지 않는 경우에 대한 처리
      throw new GlobalExceptionHandler.CustomException(ErrorCode.REFRESH_TOKEN_INVALID);
    }

    String account = claims.getSubject();

    User user = userService.findUser(account);

    res.setHeader(AUTHORIZATION_HEADER, jwtUtil.createAccessToken(user.getAccount()));

    return ResponseEntity.ok().body(new MessageDTO("토큰이 성공적으로 생성됐습니다.", 200));
  }

  @DeleteMapping("/api/users/logout")
  public ResponseEntity<?> logout(
      @CookieValue(value = "Refresh-Authorization", required = false) String token,
      HttpServletResponse res) {
    logger.info("리프레시 토큰: " + token);

    if (token == null || token.isEmpty()) {
      Cookie cookie = new Cookie("Refresh-Authorization", null);
      cookie.setPath("/");
      cookie.setHttpOnly(true);
      cookie.setMaxAge(0); // 쿠키를 즉시 만료시킵니다.
      res.addCookie(cookie);
      return ResponseEntity.ok().body(new MessageDTO("로그아웃 되었습니다.", 200));
    }

    RefreshToken refreshToken = refreshTokenService.findToken(token);

    if (refreshToken == null) {
      // 리프레시 토큰이 존재하지 않는 경우에 대한 처리
      throw new GlobalExceptionHandler.CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
    }

    Claims claims = jwtUtil.getUserInfoFromToken(refreshToken.getTokenValue());

    if (claims == null) {
      // 리프레시 토큰이 유효하지 않거나 사용자 ID를 포함하지 않는 경우에 대한 처리
      throw new GlobalExceptionHandler.CustomException(ErrorCode.REFRESH_TOKEN_INVALID);
    }

    userService.findUser(claims.getSubject());

    refreshTokenService.deleteRefreshToken(refreshToken);

    Cookie cookie = new Cookie("Refresh-Authorization", null);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge(0);
    res.addCookie(cookie);

    return ResponseEntity.ok().body(new MessageDTO("토큰이 성공적으로 삭제됐습니다.", 200));
  }
}
