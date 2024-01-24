package com.example.testypie.View.controller;

import com.example.testypie.domain.user.OAuth.OAuthToken;
import com.example.testypie.domain.user.kakao.service.KakaoService;
import com.example.testypie.global.jwt.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class ViewController {
    private final KakaoService kakaoService;

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/home/login")
    public String loginSuccess() {
        return "login";
    }

    @GetMapping("/home/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/home/kakao-login/callback")
    @ResponseBody
    public String kakaoCallback(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {//Data를 리턴해주는 컨트롤러 함수
        String token = kakaoService.kakaoLogin(code);

        // jwt토큰 만들기
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, token);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/home";
    }

    @GetMapping("/api/category/{parentName}/{childId}/products")
    public String addProduct(@PathVariable String parentName, @PathVariable Long childId, Model model) {
        model.addAttribute("parentName", parentName);
        model.addAttribute("childId", childId);
        return "addProductForm"; // 이동할 뷰의 이름을 반환
    }

    @GetMapping("/api/category/{parentName}/{childId}/products/{productId}/update")
    public String updateProduct(@PathVariable String parentName, @PathVariable Long childId, Model model, @PathVariable Long productId) {
        model.addAttribute("parentName", parentName);
        model.addAttribute("childId", childId);
        model.addAttribute("productId", productId);
        return "updateProductForm"; // 이동할 뷰의 이름을 반환
    }
}

